package dav.AdministracionDelEfectivo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.SysexMessage;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageDestino;
import dav.ActualizacionDeDatos.PageActualizacionCalendario;
import dav.AdministracionDelEfectivo.*;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.pymes.moduloCrearTx.PageValores;
import dav.transversal.DavCatalogo;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.execution.DataDriven;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dxc.util.ExcelFile;
import launchTest.Launch_AdministracionDelEfectivo;

public class ControllerDestinosMasivosAdef{
	
	PageAdministracionDelEfectivo pageAdef;
	BasePageWeb basePage;
	PageOrigen pageOrigen;
	PagePortalPymes pagePortalPymes;
	DavCatalogo catTiposDoc;
	DavCatalogo catBancos;
	PageDestino pageDestino;
	Launch_AdministracionDelEfectivo launchAdef;
	String archivoCargar; // RUTA ARCHIVO A CARGAR
	String archivoDest; // RUTA ARCHIVO CON LOS DESTINOS
	int totalDestinos;
	double valorTransaccion;

//=======================================================================================================================
	public ControllerDestinosMasivosAdef(PageAdministracionDelEfectivo pageAdef) {
		try {
			catTiposDoc = new DavCatalogo(DavCatalogo.CAT_TIPOSDOC);
			catBancos = new DavCatalogo(DavCatalogo.CAT_BANCOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pageAdef = pageAdef;
	}

	public int getTotalDestinos() {
		return this.totalDestinos;
	}

	public double getValorTransaccion() {
		return this.valorTransaccion;
	}

	By locFileCarga = By.xpath("//*[@id=\"app\"]/div/div/div[4]/div[1]/div[5]/input[1]");
	By locLinkError = By.linkText("aquí");
	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Destino']");
	By veri = By.xpath("//input[@type='submit'][@value='Continuar con errores']");
	By bntContinuarConErr = By.xpath("//input[@id='cphCuerpo_btnContinuarErrores']");
	By locPasoPageValor = By.xpath("//td[@class='TituloRojo']/b[text()='Valores']");
	By locBuscarArchivo = By.xpath("//input[@value=\"Buscar Archivo\"]");
	
//***********************************************************************************************************************
	/**
	 * Debe dejar en la pantalla de "Valores" si no hay error.<br>
	 * Retorna [null] si se cargaron lode destinos correctamente, en caso contrario
	 * retorna el error respectivo.
	 */
	public String cargarDestinos() throws Exception {

		this.totalDestinos = 0;
		this.valorTransaccion = 0;

		archivoDest = SettingsRun.getTestData().getParameter("Archivo Destinos").trim();
		String tipoCarga = SettingsRun.getTestData().getParameter("Tipo de Carga").trim();
		String filasATomar = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();
		String servUpper = SettingsRun.getTestData().getParameter("Servicio").toUpperCase();
		List<Integer> listRowExcel;

		String msgError = null;
		switch (tipoCarga) {
//-----------------------------------------------------------------------------------------------------------------------
		case "DIGITADO":
			listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
			System.out.println("a- "+ servUpper);
			msgError = this.digitarDestinos_Adef(archivoDest, listRowExcel);
			
//			if (msgError == null) // DIGIT� DESTINOS, SE DEBE VERIFICAR Y CONTINUAR
//				msgError = pageDestino.verificarContinuarCargaMasiva();
			break;
//-----------------------------------------------------------------------------------------------------------------------
		case "PLANO": // S�LO APLICA PARA N�MINA Y PROVEEDORES
			if (filasATomar.equals("N/A"))
				archivoCargar = Evidence.saveFile(archivoDest);
			else {
				listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
				archivoCargar = this.crearArchivoPlano_Adef(archivoDest, listRowExcel);
			}
			msgError = this.cargarDestinosXArchivoAdef(tipoCarga, archivoCargar);
			break;
//-----------------------------------------------------------------------------------------------------------------------
		case "EXCEL":
			if (filasATomar.equals("N/A"))
				archivoCargar = Evidence.saveFile(archivoDest);
			else {
				
				listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
				archivoCargar = this.crearArchivoExcel_Adef(archivoDest, listRowExcel);
				
			}
//			listRowExcel  = DXCUtil.getListaDatos(filasATomar, ",");
			msgError = this.cargarDestinosXArchivoAdef(tipoCarga, archivoCargar);
			break;

		case "Archivo existente":
			msgError = pageDestino.cargarDestinosXArchivo(tipoCarga, archivoDest);
			break;
		}
		return msgError;
	}
//***********************************************************************************************************************		

	private String digitarDestinos_Adef(String archivoDatos, List<Integer> listRowExcel) throws Exception {
		FileInputStream inputStream = new FileInputStream(archivoDatos);
		Workbook workbook = getWorkbook(inputStream, archivoDatos);
		Sheet sheet = workbook.getSheetAt(0);
		
		int posCtasDestino = 0;
		
		String[] tipoDestino = new String[listRowExcel.size()];
		String[] numCtasDestino = new String[listRowExcel.size()];
		String[] valDestino = new String[listRowExcel.size()];

		for (int rowIndex : listRowExcel) {
			Row row = sheet.getRow(rowIndex - 1);
			if (row != null) {
	
				String[] values = getRowCellValues(row, 9);
				values[7] = values[7].substring(0, values[7].length()-2);
				
				String msgError = validateAndAdicionarDestino_Adef(values);
				
				tipoDestino[posCtasDestino] = values[5];
				numCtasDestino[posCtasDestino] = values[6];
				
				if (msgError == null) {
					
					valDestino[posCtasDestino] = values[7];
					posCtasDestino++;
					
					this.valorTransaccion += Double.valueOf(values[7]);
					this.totalDestinos++;
	
				} else {
					Reporter.write("Fila [" + rowIndex + "] del Excel : " + msgError);
				}
			}
		}
		
		for (String data : tipoDestino) {
			System.out.println(data);
		}
		
		for (String data : numCtasDestino) {
			System.out.println(data);
		}
		
		for (String data : valDestino) {
			System.out.println(data);
		}
		
		Launch_AdministracionDelEfectivo.tipoDestino = tipoDestino;
		Launch_AdministracionDelEfectivo.ctasDestino = numCtasDestino;
		Launch_AdministracionDelEfectivo.valDestino = valDestino;
		
		return null;
	}

	private Workbook getWorkbook(FileInputStream inputStream, String archivoDatos) throws IOException {
		Workbook workbook;
		if (archivoDatos.endsWith(".xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (archivoDatos.endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("El archivo no es un archivo de Excel");
		}
		return workbook;
	}

	private String[] getRowCellValues(Row row, int numCells) {
		String[] values = new String[numCells];
		for (int i = 0; i < numCells; i++) {
			Cell cell = row.getCell(i);
			values[i] = (cell != null) ? getCellValueAsString(cell) : "";
		}
		return values;
	}

	private String getCellValueAsString(Cell cell) {
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			double numericValue = cell.getNumericCellValue();
			return Double.toString(numericValue);
		} else {
			return "";
		}
	}

	private String validateAndAdicionarDestino_Adef(String[] values) throws Exception {
		
		String tipoId = values[0];
		String numId = values[1];
		String nombre = values[2];
		String apellido = values[3];
		String bancoDest = values[4];
		String tipoCtades = values[5];
		String numProdes = values[6];
		String valorPago = values[7];
		String referencia = values[8];

		if (hasEmptyValues(bancoDest, tipoCtades, numProdes, nombre, tipoId, numId, valorPago)) {
			return "Falta información en la fila";
		}
		
		return pageAdef.adicionarDestino(tipoId, numId, nombre, apellido, bancoDest, tipoCtades, numProdes,
				valorPago, referencia);
	}

	private boolean hasEmptyValues(String... values) {
		for (String value : values) {
			if (value.isEmpty()) {
				return true;
			}
		}
		return false;
	}

//***********************************************************************************************************************	
	// String archivoDatos = "C:\\Users\\pvega2\\OneDrive - DXC
	// Production\\Desktop\\crear txt\\01 Destinos PagosN�m-Prov.xls";
	private String crearArchivoPlano_Adef(String archivoDatos, List<Integer> listRowExcel) throws Exception {

		String tipoDocEmpr = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		String numDocEmpr = SettingsRun.getTestData().getParameter("Número ID Empresa").trim();
		String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		String tipoCta = "";
//		String tipoCta = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
		String sucursal = SettingsRun.getTestData().getParameter("Sucursal").trim();

		String header = getHeader(tipoDocEmpr, numDocEmpr, servicio, tipoCta, sucursal);
		String[] arrContenido = getContenido(archivoDatos, listRowExcel); // 0-contenido, 1-totalDest, 2-valorTotal
		String conten = arrContenido[0];
		header = header.replace("TOTAL_TRAS", arrContenido[1]).replace("VALOR_TRAS", arrContenido[2]);

		this.totalDestinos = Integer.valueOf(arrContenido[1]);
		this.valorTransaccion = Double.valueOf(arrContenido[2]);

		String ruta = Evidence.getStartEvidenceName() + "ArchPlano.txt"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
		File file = new File(ruta);
		if (!file.exists())
			file.createNewFile();

		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(header);
		bw.write(conten);
		bw.close();
		return ruta;
	}

//***********************************************************************************************************************
	private String getHeader(String tipoDocEmpr, String numDocEmpr, String servicio, String tipoCtaOrigen,
			String sucursal) throws Exception {

		// ENCABEZADO O ARCHIVO DE CONTROL
		String numEmp = DXCUtil.leftComplete(numDocEmpr, 16, '0');// 16 caracteres
		String tipoPag = "ADEFADEF";
		String ctaOrigen = DXCUtil.leftComplete(sucursal, 16, '0');// 16 caracteres
		String tipCta = "CE";// 2 caracteres
		String codBan = DXCUtil.leftComplete("51", 6, '0');// 6 caracteres SIEMPRE 51
		String valorTras = "VALOR_TRAS";// 18 caracteres
		String numTottras = "TOTAL_TRAS";
		DXCUtil.leftComplete("0", 6, '0');// 6 caracteres
		String fechpro = DXCUtil.dateToString("YYYYMMDD");
		String horapro = DXCUtil.hourToString("HH:mm:ss").replace(":", "");// 6 caracteres
		String complem1 = "000099990000000000000000"; // 4-0-codOpe, 4-9-codNoProceso, 4-0-fechaGen, 4-0-horaGen,
														// 2-0-indic
		String tipid = "";
		String complem2 = DXCUtil.leftComplete("0", 56, '0');// 12-0-Numerocliente, 4-0-oficRecaudo, 40-0-campoFutur
//-----------------------------------------------------------------------------------------------------------------------
		// SE VALIDA EL SERVICIO Y SE LE ASIGNA EL CODIGO
		servicio = DXCUtil.removeAccents(servicio).toUpperCase();
//-----------------------------------------------------------------------------------------------------------------------
		// SE VALIDA EL TIPO DE CUENTA Y SE LE ASIGNA EL CODIGO
//		tipCta = getCodigoTipoProducto(tipoCtaOrigen);
//-----------------------------------------------------------------------------------------------------------------------
		String codTipid = catTiposDoc.getCodigoXNombre(DXCUtil.removeAccents(tipoDocEmpr).toUpperCase());
		tipid = DXCUtil.leftComplete(codTipid, 2, '0');
		;
//-----------------------------------------------------------------------------------------------------------------------
		return "RC" + numEmp + tipoPag + ctaOrigen + tipCta + codBan + valorTras + numTottras + fechpro + horapro
				+ complem1 + tipid + complem2;
	}

//***********************************************************************************************************************
	// 0-contenido, 1-totalDest, 2-valorTotal
	private String[] getContenido(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		DataDriven dd = new DataDriven(archivoDatos);
		String ref, numiddest, numPro, codBanDest, tipoId, CodTipoId, valorTx, ingresoCta = "", talon, trasach,
				resulPros, complem3;
		
		String[] tipoDestino = new String[listaFilasExcel.size()];
		String[] numCtasDestino = new String[listaFilasExcel.size()];
		String[] valDestino = new String[listaFilasExcel.size()];
		
		int posCtasDestino = 0;
		int totalDest = 0;
		double valorTotal = 0;
		for (int fila : listaFilasExcel) {
			
			tipoDestino[posCtasDestino] = dd.getParameterByRow("tipoProdDestino", fila);
			numCtasDestino[posCtasDestino] = dd.getParameterByRow("numProdDestino", fila);
			valDestino[posCtasDestino] = dd.getParameterByRow("valorPago", fila);
			posCtasDestino++;

			ref = DXCUtil.leftComplete("0", 16, '0');// 16 caracteres.
			numiddest = DXCUtil.leftComplete(dd.getParameterByRow("numId", fila), 16, '0');
			numPro = DXCUtil.leftComplete(dd.getParameterByRow("numProdDestino", fila), 16, '0');
			valorTx = DXCUtil.leftComplete(dd.getParameterByRow("valorPago", fila).trim(), 18, '0');
			talon = "000000";
			codBanDest = "";
			resulPros = "9999";
			complem3 = DXCUtil.leftComplete("0", 81, '0');

			tipoId = dd.getParameterByRow("tipoId", fila);
			CodTipoId = DXCUtil.leftComplete(catTiposDoc.getCodigoXNombre(DXCUtil.removeAccents(tipoId).toUpperCase()),
					2, '0');

			String tipPro = dd.getParameterByRow("tipoProdDestino", fila);
			String codPro = getCodigoTipoProducto(tipPro);
//-----------------------------------------------------------------------------------------------------------------------
			String nbBanco = dd.getParameterByRow("bancoDestino", fila).toUpperCase();
			String numBan = catBancos.getCodigoXNombreContained(nbBanco);
			codBanDest = DXCUtil.leftComplete(numBan, 6, '0');// 6 caracteres SIEMPRE 51

			if (nbBanco.contains("DAVIVIENDA")) {
				trasach = "0";
			} else {
				trasach = "1";
			}
			ingresoCta = ingresoCta + "\r\nTR" + numiddest + ref + numPro + codPro + codBanDest + valorTx + talon
					+ CodTipoId + trasach + resulPros + complem3;

			totalDest++;
			valorTotal += Double.valueOf(valorTx);
		}
//-----------------------------------------------------------------------------------------------------------------------
		Launch_AdministracionDelEfectivo.tipoDestino = tipoDestino;
		Launch_AdministracionDelEfectivo.ctasDestino = numCtasDestino;
		Launch_AdministracionDelEfectivo.valDestino = valDestino;
		String numTottras = DXCUtil.leftComplete(String.valueOf(totalDest), 6, '0'); // 6 caracteres
		String valorTras = DXCUtil.leftComplete(DXCUtil.formatPlano(valorTotal), 18, '0'); // 18 caracteres
		return new String[] { ingresoCta, numTottras, valorTras };
	}

//***********************************************************************************************************************
	private String getCodigoTipoProducto(String tipoProducto) {
		String tipoProd = DXCUtil.removeAccents(tipoProducto).toUpperCase();
		String codPro = "";
		if (tipoProd.contains("ELECTRONICOS")) {
			codPro = "DE";
		} else if (tipoProd.contains("PREPAGO") || tipoProd.contains("MAESTRO") || tipoProd.contains("TPM")) {
			codPro = "TP";
		} else if (tipoProd.contains("CORRIENTE") || tipoProd.contains("CREDIPLUS")) {
			codPro = "CC";
		} else if (tipoProd.contains("AHORRO")) {
			codPro = "CA";
		} else if (tipoProd.contains("DAVIPLATA")) {
			codPro = "DP";
		} else if (tipoProd.contains("TARJETA")) {
			codPro = "TC";
		}	
			
		return codPro;

	}

	// ***********************************************************************************************************************
	private String getCodigoTipoId(String tipoid) {
		String tipoIdProd = DXCUtil.removeAccents(tipoid).toUpperCase();
		String codPro = "NA";
		if (tipoIdProd.contains("CEDULA DE CIUDADANIA")) {
			codPro = "1";
		} else if (tipoIdProd.contains("NIT")) {
			codPro = "3";

		} else if (tipoIdProd.contains("TARJETA")) {
			codPro = "2";

		} else
			codPro = "0";

		return codPro;

	}
//======================================================================================================================================
//	String archivoDatos = "C:\\Users\\pvega2\\OneDrive - DXC Production\\Desktop\\Data\\01 Destinos PagosN�m-Prov.xls";

	private String crearArchivoExcel_Adef(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		String[] header = { "Tipo de identificación", "Número de identificación", "Nombre", "Apellido",
				"Código del banco", "Tipo de producto", "Número del producto", "Valor de distribución","Referencia","Correo electrónico","Descripción o detalle"};

		String[] params = { "tipoId", "numId", "nombre", "apellido", "bancoDestino", "tipoProdDestino",
				"numProdDestino", "valorPago", "referencia","CorreoElectronico","descripción" };

		return crearExcel(archivoDatos, listaFilasExcel, header, params);

	}

	private String crearExcel(String archivoSource, List<Integer> listaFilasSourceExcel, String[] header,
			String[] paramsSource) throws Exception {

		String ruta = Evidence.getStartEvidenceName() + "ArchExcel.xlsx"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
		ExcelFile excelFile = new ExcelFile(ruta, ExcelFile.CREATE);
		excelFile.createSheet("Destinos");
		
		int posCtasDestino = 0;
		
		String[] tipoDestino = new String[listaFilasSourceExcel.size()];
		String[] numCtasDestino = new String[listaFilasSourceExcel.size()];
		String[] valDestino = new String[listaFilasSourceExcel.size()];
		
		int totalCols = header.length;
		
		for (int posH = 0; posH < totalCols; posH++) {
			excelFile.setStringCellValue(1, posH + 1, header[posH]);
		}
		DataDriven dd = new DataDriven(archivoSource);
		int filaAD = 2;
		String paramUpper, dato;
		for (int fila : listaFilasSourceExcel) {
			for (int posH = 0; posH < totalCols; posH++) {
				paramUpper = paramsSource[posH].toUpperCase();
				dato = dd.getParameterByRow(paramsSource[posH], fila);
				if (paramUpper.contains("TIPO") && paramUpper.contains("IDENTIFICACION")
						|| paramUpper.contains("TIPOID"))
					dato = this.getCodigoTipoId(dato);
				else if ((paramUpper.contains("BANCO") && paramUpper.contains("DEST")))
					dato = catBancos.getCodigoXNombreContained(dato);
				else if ((paramUpper.contains("TIPO") && paramUpper.contains("PROD"))) {
					tipoDestino[posCtasDestino] = dato;
					dato = this.getCodigoTipoProducto(dato);
				}
				else if ((paramUpper.contains("REFERENCIA")))
					dato = this.getCodigoTipoProducto(dato);
				else if ((paramUpper.contains("CORREOELECTRONICO")))
					dato = this.getCodigoTipoProducto(dato);
				else if ((paramUpper.contains("DESCRIPCION")))
					dato = this.getCodigoTipoProducto(dato);
				else if ((paramUpper.contains("NUM") && paramUpper.contains("PROD"))) {
					numCtasDestino[posCtasDestino] = dato;
				}
				if (paramUpper.equals("VALORPAGO") && !dato.isEmpty()) {
					dato = dato.substring(0, dato.length()-2);
					valDestino[posCtasDestino] = dato;
					this.valorTransaccion += Double.valueOf(dato);
					posCtasDestino++;
				}

				excelFile.setStringCellValue(filaAD, posH + 1, dato);
			}
			filaAD++;
			this.totalDestinos++;
		}
		
		Launch_AdministracionDelEfectivo.tipoDestino = tipoDestino;
		Launch_AdministracionDelEfectivo.ctasDestino = numCtasDestino;
		Launch_AdministracionDelEfectivo.valDestino = valDestino;
		
		excelFile.saveAndCloseFile();
		return ruta;
	}
	
	public String cargarDestinosXArchivoAdef(String formato, String nbArchivoCargar) throws Exception {

//		this.waitPantallaDestino();

//		String formatoArchivo = "Archivo Plano";
//		if (formato.equals("EXCEL"))
//			formatoArchivo = "Excel";
//		this.selectListItemExacto(locFormatoArchivo, formatoArchivo);
		
		do {
		} while (this.pageAdef.element(locFileCarga) == null);
		
		Evidence.save("Pagina para cargar destinos");
		
		do {
		} while (!pageAdef.isDisplayed(locBuscarArchivo));
		
		this.pageAdef.mouseOver(locBuscarArchivo);
		this.pageAdef.mouseClick();
		
//		this.pageAdef.click(locBuscarArchivo);                                                            
//		String scriptClicIngresar1 = "document.querySelector(\"#app > div > div > div:nth-child(5) > div.row.mb-4 > div:nth-child(5) > input.button--secundary\").click()";
//		this.pageAdef.getJse().executeScript(scriptClicIngresar1);
		
		DXCUtil.wait(5);
		
		this.adjuntarArchivoAdef(nbArchivoCargar);
		
		this.pageAdef.clickButton("Cargar Archivo");

		String msgAlerta;
		
		msgAlerta = this.pageAdef.MsgAlerta();
		
		DXCUtil.wait(3);
//-----------------------------------------------------------------------------------------------------------------------
		if (msgAlerta != null && msgAlerta.contains("El archivo no ha podido cargarse")) {
			String idWindActual = this.pageAdef.getIdWindow();
			this.pageAdef.click(locLinkError);
			List<String> listWindows;
			do {
				listWindows = this.pageAdef.getIdWindows();
			} while (listWindows.size() == 1);
			this.pageAdef.changeWindow(listWindows.get(1));
			Evidence.saveAllScreens("ErrorCarga", this.pageAdef);
			this.pageAdef.closeCurrentBrowser();
			this.pageAdef.changeWindow(idWindActual);
			return "Error en la carga del archivo";
		}
//-----------------------------------------------------------------------------------------------------------------------
		// SI LLEGA A ESTE PUNTO PUDO CARGAR EL ARCHIVO
		//return this.verificarContinuarCargaMasiva();
		 return "ok";
	}

	public String verificarContinuarCargaMasiva() throws Exception {
		// SI LLEGA A ESTE PUNTO PUDO CARGAR EL ARCHIVO
		Evidence.saveFullPage("ProdDestinos", pageAdef);
		DXCUtil.wait(4);
		this.pageAdef.clickButton("Verificar y Continuar");
//-----------------------------------------------------------------------------------------------------------------------
		String msgAlerta;
		WebElement elementPaso, conerror;
		int contador = 1;
		do { // ESPERA A QUE MUESTRE LA PANTALLA DE VALORES O ALERTA
			DXCUtil.wait(1);
			contador++;
			conerror = pageAdef.element(bntContinuarConErr);
			elementPaso = pageAdef.element(locPasoPageValor);
			msgAlerta = this.pageAdef.MsgAlerta();
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				if (conerror == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgAlerta);
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el botón: Verificar y Continuar");
				}
				this.terminarIteracion();
			}
		} while (elementPaso == null && msgAlerta == null && conerror == null);
		Reporter.reportEvent(Reporter.MIC_INFO, msgAlerta);
		if (elementPaso == null && msgAlerta == null || conerror != null
				|| msgAlerta.contains("El archivo se cargó exitosamente")) { // HAY ERROR DE VERIFICACI�N
			Evidence.saveAllScreens("ErrorVerificando", pageAdef);
			DXCUtil.wait(2);
			if (msgAlerta.contains("errores")) {
				do {
					DXCUtil.wait(1);
					contador++;
					if (contador > 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
						if (conerror == null) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msgAlerta);
							Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el botón: Continuar con errores");
						}

						this.terminarIteracion();
					}
				} while (pageAdef.element(veri) == null);
				pageAdef.clickButton("Continuar con errores");
			}

		}
		return null;
	}
	
	private String getTipoCuenta(String bancoDest, String tipoCta) {

		String tipoCtaReal = tipoCta;
		if (bancoDest.equals("DAVIVIENDA") && tipoCta.contains("OTRO")) {
			if (tipoCta.contains("AHORRO"))
				tipoCtaReal = "CUENTA DE AHORROS";
			else if (tipoCta.contains("CORRIENTE"))
				tipoCtaReal = "CUENTA CORRIENTE";
		} else if (!bancoDest.equals("DAVIVIENDA") && !tipoCta.contains("OTRO") && !tipoCta.contains("ELECTR")) {
			if (tipoCta.contains("AHORRO"))
				tipoCtaReal = "CUENTAS AHORRO OTROS BANCOS";
			else if (tipoCta.contains("CORRIENTE"))
				tipoCtaReal = "CUENTAS CORRIENTES OTROS BANCOS";
			else
				tipoCtaReal = "DEPÓSITOS ELECTRONICOS";
		}
		return tipoCtaReal;
	}
	
	public void waitPantallaDestino() throws AWTException {
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL DESTINO
			DXCUtil.wait(1);
			elementPaso = this.pageDestino.element(locPasoPage);
		} while (elementPaso == null);

	}
	
	public String getMsgAlertIfExist(String... idsAlerta) {
		String msgAlert = null;
		for (String id : idsAlerta) {
			By locMessage = By.id(id);
			if (pageAdef.isDisplayed(locMessage)) {
				DXCUtil.wait(1);
				msgAlert = pageAdef.getText(locMessage).trim();
				if (msgAlert.equals("")) // NO HAY MENSAJE
					msgAlert = null;
				else
					break; // PARA TERMINAR EL CICLO
			}
		}
		return msgAlert;
	}

	public void adjuntarArchivoAdef(String ruta) throws Exception {

        DXCUtil.wait(2);
        
        if (!ruta.isEmpty()) {

            DXCUtil.wait(3);

            try {
                Robot action = new Robot();

                StringSelection selection = new StringSelection(ruta);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

                action.keyPress(KeyEvent.VK_CONTROL);
                action.keyPress(KeyEvent.VK_V);

                action.keyRelease(KeyEvent.VK_CONTROL);
                action.keyRelease(KeyEvent.VK_V);

                action.keyPress(KeyEvent.VK_ENTER);
                action.keyRelease(KeyEvent.VK_ENTER);
                
                DXCUtil.wait(2);
                
                Evidence.save("Archivo adjunto");

            } catch (Exception e) {
                Reporter.reportEvent(Reporter.MIC_FAIL, "Error, verifique o comuniquese con automatizador de dxc.");
                e.printStackTrace();
            }

        } else {
            Reporter.reportEvent(Reporter.MIC_FAIL,
                    "No se ha encontrado una ruta esfecifica para realizar el cargue, por favor verifique el documento.");
        }
	}
	
	public void terminarIteracion() throws Exception {
		pageAdef.getDriver().switchTo().defaultContent();
		pageAdef.pageLogin.terminarIteracion();
	}
	
}
