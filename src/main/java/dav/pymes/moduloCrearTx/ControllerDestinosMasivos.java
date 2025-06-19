package dav.pymes.moduloCrearTx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dav.Recaudos.PageRecaudos;
import dav.transversal.DavCatalogo;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.library.data.DataDriven;
import dxc.execution.DataDriven;
//import dxc.library.data.DataDrivenExcel;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;
import dxc.util.ExcelFile;

public class ControllerDestinosMasivos {

	DavCatalogo catTiposDoc;
	DavCatalogo catBancos;
	PageDestino pageDestino;
	String archivoCargar; // RUTA ARCHIVO A CARGAR
	String archivoDest; // RUTA ARCHIVO CON LOS DESTINOS
	int totalDestinos;
	double valorTransaccion;
	List<Integer> listRowExcel;
	static String ruta;

// ======================================================================================================================

// ===========================================[ControllerDestinosMasivos]===========================================================================

	public ControllerDestinosMasivos(PageDestino pageDestino) {
		try {
			catTiposDoc = new DavCatalogo(DavCatalogo.CAT_TIPOSDOC);
			catBancos = new DavCatalogo(DavCatalogo.CAT_BANCOS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pageDestino = pageDestino;
	}

	public int getTotalDestinos() {
		return this.totalDestinos;
	}

	public double getValorTransaccion() {
		return this.valorTransaccion;
	}

// ======================================================================================================================

// ===========================================[cargarDestinos]===========================================================================

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

		if (!DXCUtil.ArchivoExist(archivoDest)) {
			return "El sistema no puede encontrar el archivo especificado: " + archivoDest;
		}

		String msgError = null;
		switch (tipoCarga) {
//-----------------------------------------------------------------------------------------------------------------------
		case "DIGITADO":
			listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
			if (servUpper.contains("NÓMI") || servUpper.contains("NÓMINA") || servUpper.contains("PROVE")
					|| servUpper.contains("PROVEEDORES"))
				msgError = this.digitarDestinos_NomProv(archivoDest, listRowExcel);
			else if (servUpper.contains("AFC"))
				msgError = this.digitarDestinos_CtasAFC(archivoDest, listRowExcel);
			else if (servUpper.contains("CRÉD") || servUpper.contains("CRÉDITO.3ROS"))
				msgError = this.digitarDestinos_Cred3ros(archivoDest, listRowExcel);
			if (msgError == null) // DIGITÓ DESTINOS, SE DEBE VERIFICAR Y CONTINUAR
				msgError = pageDestino.verificarContinuarCargaMasiva();
			break;
//-----------------------------------------------------------------------------------------------------------------------
		case "PLANO": // SÓLO APLICA PARA NÓMINA Y PROVEEDORES
			if (filasATomar.equals("N/A"))
				archivoCargar = Evidence.saveFile(archivoDest);
			else {
				listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
				archivoCargar = this.crearArchivoPlano_NomProv(archivoDest, listRowExcel);
			}
			msgError = pageDestino.cargarDestinosXArchivo(tipoCarga, archivoCargar);
			break;
//-----------------------------------------------------------------------------------------------------------------------
		case "EXCEL":
			if (filasATomar.equals("N/A"))
				archivoCargar = Evidence.saveFile(archivoDest);
			else {
				listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");

				if (servUpper.contains("NÓMI") || servUpper.contains("NÓMINA") || servUpper.contains("PROVE")
						|| servUpper.contains("PROVEEDORES"))
					archivoCargar = this.crearArchivoExcel_NomProv(archivoDest, listRowExcel);
				else if (servUpper.contains("AFC"))
					archivoCargar = this.crearArchivoExcel_CtasAFC(archivoDest, listRowExcel);
				else if (servUpper.contains("CRÉD") || servUpper.contains("CRÉDITO.3ROS"))
					archivoCargar = this.crearArchivoExcel_Cred3ros(archivoDest, listRowExcel);
			}
//			listRowExcel  = DXCUtil.getListaDatos(filasATomar, ",");
			msgError = pageDestino.cargarDestinosXArchivo(tipoCarga, archivoCargar);
			break;

		case "Archivo existente Plano":
			msgError = pageDestino.cargarDestinosXArchivo("PLANO", archivoDest);
			break;
		case "Archivo existente Excel":
			msgError = pageDestino.cargarDestinosXArchivo("EXCEL", archivoDest);
			break;
		}
		return msgError;
	}

// ======================================================================================================================

// ===========================================[digitarDestinos_NomProv]===========================================================================

	private String digitarDestinos_NomProv(String archivoDatos, List<Integer> listRowExcel) throws Exception {
		FileInputStream inputStream = new FileInputStream(archivoDatos);
		Workbook workbook = getWorkbook(inputStream, archivoDatos);
		Sheet sheet = workbook.getSheetAt(0);

		for (int rowIndex : listRowExcel) {
			Row row = sheet.getRow(rowIndex - 1);
			if (row != null) {
				String[] values = getRowCellValues(row, 9);
				String msgError = validateAndAdicionarDestino_NomProv(values);

				if (msgError == null) {
					this.valorTransaccion += Double.valueOf(values[7]);
					this.totalDestinos++;
				} else {
					Reporter.write("Fila [" + rowIndex + "] del Excel : " + msgError);
				}
			}
		}
		return null;
	}

// ======================================================================================================================

// ===========================================[digitarDestinos_CtasAFC]===========================================================================

	private String digitarDestinos_CtasAFC(String archivoDatos, List<Integer> listRowExcel) throws Exception {
		FileInputStream inputStream = new FileInputStream(archivoDatos);
		Workbook workbook = getWorkbook(inputStream, archivoDatos);
		Sheet sheet = workbook.getSheetAt(0);

		for (int rowIndex : listRowExcel) {
			Row row = sheet.getRow(rowIndex - 1);
			if (row != null) {
				String[] values = getRowCellValues(row, 5);
				String msgError = validateAndAdicionarDestino_CtasAFC(values);

				if (msgError == null) {
					this.valorTransaccion += Double.valueOf(values[4]);
					this.totalDestinos++;
				} else {
					Reporter.write("Fila [" + rowIndex + "] del Excel : " + msgError);
				}
			}
		}
		return null;
	}

// ======================================================================================================================

// ===========================================[digitarDestinos_Cred3ros]===========================================================================

	private String digitarDestinos_Cred3ros(String archivoDatos, List<Integer> listRowExcel) throws Exception {
		FileInputStream inputStream = new FileInputStream(archivoDatos);
		Workbook workbook = getWorkbook(inputStream, archivoDatos);
		Sheet sheet = workbook.getSheetAt(0);

		for (int rowIndex : listRowExcel) {
			Row row = sheet.getRow(rowIndex - 1);
			if (row != null) {
				String[] values = getRowCellValues(row, 5);
				String msgError = validateAndAdicionarCred3ros(values);

				if (msgError == null) {
					this.valorTransaccion += Double.valueOf(values[4]);
					this.totalDestinos++;
				} else {
					Reporter.write("Fila [" + rowIndex + "] del Excel : " + msgError);
				}
			}
		}
		return null;
	}

// ======================================================================================================================

// ===========================================[getWorkbook]===========================================================================

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

// ======================================================================================================================

// ===========================================[getRowCellValues]===========================================================================

	private String[] getRowCellValues(Row row, int numCells) {
		String[] values = new String[numCells];
		for (int i = 0; i < numCells; i++) {
			Cell cell = row.getCell(i);
			values[i] = (cell != null) ? getCellValueAsString(cell) : "";
		}
		return values;
	}

// ======================================================================================================================

// ===========================================[getCellValueAsString]===========================================================================

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

// ======================================================================================================================

// ===========================================[validateAndAdicionarDestino_NomProv]===========================================================================

	private String validateAndAdicionarDestino_NomProv(String[] values) throws Exception {
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

		return pageDestino.adicionarDestino(tipoId, numId, nombre, apellido, bancoDest, tipoCtades, numProdes,
				valorPago, referencia);
	}

// ======================================================================================================================

// ===========================================[validateAndAdicionarDestino_CtasAFC]===========================================================================

	private String validateAndAdicionarDestino_CtasAFC(String[] values) throws Exception {
		String numeroAFC = values[0];
		String titular = values[1];
		String numId = values[2];
		String valorAporte = values[3];
		String valorCuotaCont = values[4];

		if (hasEmptyValues(numeroAFC, titular, numId, valorAporte, valorCuotaCont)) {
			return "Falta información en la fila";
		}

		return pageDestino.adicionarDestinoAFC(numeroAFC, titular, numId, valorAporte, valorCuotaCont);
	}

// ======================================================================================================================

// ===========================================[validateAndAdicionarCred3ros]===========================================================================

	private String validateAndAdicionarCred3ros(String[] values) throws Exception {
		String tipoProdDestino = values[0];
		String numProdDestino = values[1];
		String titular = values[2];
		String numDocumento = values[3];
		String valorPagar = values[4];

		if (hasEmptyValues(tipoProdDestino, numProdDestino, titular, numDocumento, valorPagar)) {
			return "Falta información en la fila";
		}

		return pageDestino.adicionarDestinoCred3ros(tipoProdDestino, numProdDestino, titular, numDocumento, valorPagar);
	}

//-----------------------------------------------------------------------------------------------------------------------

	private boolean hasEmptyValues(String... values) {
		for (String value : values) {
			if (value.isEmpty()) {
				return true;
			}
		}
		return false;
	}

// ======================================================================================================================

// ===========================================[crearArchivoPlano_NomProv]===========================================================================

	// String archivoDatos = "C:\\Users\\pvega2\\OneDrive - DXC
	// Production\\Desktop\\crear txt\\01 Destinos PagosNóm-Prov.xls";
	private String crearArchivoPlano_NomProv(String archivoDatos, List<Integer> listRowExcel) throws Exception {

		String tipoDocEmpr = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		String numDocEmpr = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		String tipoCta = SettingsRun.getTestData().getParameter("Tipo  oproductorigen / Franquicia").trim();
		String numCta = SettingsRun.getTestData().getParameter("Número producto origen").trim();

		String header = getHeader(tipoDocEmpr, numDocEmpr, servicio, tipoCta, numCta);
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

// ======================================================================================================================

// ===========================================[getHeader]===========================================================================

	private String getHeader(String tipoDocEmpr, String numDocEmpr, String servicio, String tipoCtaOrigen,
			String numCtaOrigen) throws Exception {

		// ENCABEZADO O ARCHIVO DE CONTROL
		String numEmp = DXCUtil.leftComplete(numDocEmpr, 16, '0');// 16 caracteres
		String tipoPag = "";
		String ctaOrigen = DXCUtil.leftComplete(numCtaOrigen, 16, '0');// 16 caracteres
		String tipCta = "";// 2 caracteres
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
		String codServ = "NA";
		if (servicio.contains("NOMINA")) {
			codServ = "NOMINOMI";
		} else if (servicio.contains("PROVEEDORES")) {
			codServ = "PROVPROV";
		}
		tipoPag = codServ;
//-----------------------------------------------------------------------------------------------------------------------
		// SE VALIDA EL TIPO DE CUENTA Y SE LE ASIGNA EL CODIGO
		tipCta = getCodigoTipoProducto(tipoCtaOrigen);
//-----------------------------------------------------------------------------------------------------------------------
		String codTipid = catTiposDoc.getCodigoXNombre(DXCUtil.removeAccents(tipoDocEmpr).toUpperCase());
		tipid = DXCUtil.leftComplete(codTipid, 2, '0');
		;
//-----------------------------------------------------------------------------------------------------------------------
		return "RC" + numEmp + tipoPag + ctaOrigen + tipCta + codBan + valorTras + numTottras + fechpro + horapro
				+ complem1 + tipid + complem2;
	}

// ======================================================================================================================

// ===========================================[getContenido]===========================================================================

	// 0-contenido, 1-totalDest, 2-valorTotal
	private String[] getContenido(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		DataDriven dd = new DataDriven(archivoDatos);
		String ref, numiddest, numPro, codBanDest, tipoId, CodTipoId, valorTx, ingresoCta = "", talon, trasach,
				resulPros, complem3;
		int totalDest = 0;
		double valorTotal = 0;
		for (int fila : listaFilasExcel) {

			ref = DXCUtil.leftComplete("0", 16, '0');// 16 caracteres.
			numiddest = DXCUtil.leftComplete(dd.getParameterByRow("numId", fila), 16, '0');
			numPro = DXCUtil.leftComplete(dd.getParameterByRow("numProdDestino", fila), 16, '0');
			valorTx = DXCUtil.leftComplete(dd.getParameterByRow("valorPago", fila).trim(), 18, '0');
			if (valorTx.contains(",")) {
				valorTx = valorTx.replace(",", ".");
			}
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
		String numTottras = DXCUtil.leftComplete(String.valueOf(totalDest), 6, '0'); // 6 caracteres
		String valorTras = DXCUtil.leftComplete(DXCUtil.formatPlano(valorTotal), 18, '0'); // 18 caracteres
		return new String[] { ingresoCta, numTottras, valorTras };
	}

// ======================================================================================================================

// ===========================================[getCodigoTipoProducto]===========================================================================

	private String getCodigoTipoProducto(String tipoProducto) {
		String tipoProd = DXCUtil.removeAccents(tipoProducto).toUpperCase();
		String codPro = "NA";
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

		} else
			codPro = "CR";

		return codPro;

	}

// ======================================================================================================================

// ===========================================[getCodigoTipoId]===========================================================================

	private String getCodigoTipoId(String tipoid) {
		String tipoIdProd = DXCUtil.removeAccents(tipoid).toUpperCase();
		String codPro = "NA";
		if (tipoIdProd.contains("CEDULA DE CIUDADANIA")) {
			codPro = "1";
		} else if (tipoIdProd.contains("NIT")) {
			codPro = "3";

		} else if (tipoIdProd.contains("CEDULA DE EXTRANJERIA")) {
			codPro = "2";

		} else
			codPro = "0";

		return codPro;

	}

// ======================================================================================================================

//==========================================[crearArchivoExcel_Referencias1Recaudos]=============================================
	private String crearArchivoExcel_Referencias1Recaudos(String archivoDatos, List<Integer> listaFilasExcel)
			throws Exception {

		String[] params = { "Codigo de Empresa", "Referencia 1", "Valor a pagar", "Fecha límite" };
		String[] header = { "Código de Empresa", "Referencia 1", "Valor a pagar", "Fecha límite" };

		return crearExcelReferencias1Recaudos(archivoDatos, listaFilasExcel, header, params);

	}

	// ===========================================[crearExcelReferencias1Recaudos]===========================================================================

	private String crearExcelReferencias1Recaudos(String archivoSource, List<Integer> listaFilasSourceExcel,
			String[] header, String[] paramsSource) throws Exception {

		String ruta = Evidence.getStartEvidenceName() + "ArchExcel.xlsx"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
		ExcelFile excelFile = new ExcelFile(ruta, ExcelFile.CREATE);
		excelFile.createSheet("Destinos");
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
				if (paramUpper.contains("Codigo de Empresa"))
					dato = this.getCodigoTipoId(dato);
				else if ((paramUpper.contains("Referencia 1")))
					dato = catBancos.getCodigoXNombreContained(dato);
				else if ((paramUpper.contains("Valor a pagar")))
					dato = this.getCodigoTipoProducto(dato);
				if (paramUpper.equals("Fecha límite"))
					dato = this.getCodigoTipoProducto(dato);

				excelFile.setStringCellValue(filaAD, posH + 1, dato);
			}
			filaAD++;
			this.totalDestinos++;
		}
		SettingsRun.getTestData().setParameter("rutaArchivo", ruta);
		excelFile.saveAndCloseFile();
		return ruta;
	}

	// ==========================================[crearArchivoExcel_Referencias1Recaudos]=============================================
	private String crearArchivoExcel_Referencias2Recaudos(String archivoDatos, List<Integer> listaFilasExcel)
			throws Exception {

		String[] params = { "Codigo de Empresa", "Referencia 1", "Referencia 2", "Valor a pagar", "Fecha límite" };
		String[] header = { "Código de Empresa", "Referencia 1", "Referencia 2", "Valor a pagar", "Fecha límite" };

		return crearExcelReferencias2Recaudos(archivoDatos, listaFilasExcel, header, params);

	}

	// ===========================================[crearExcelReferencias1Recaudos]===========================================================================

	private String crearExcelReferencias2Recaudos(String archivoSource, List<Integer> listaFilasSourceExcel,
			String[] header, String[] paramsSource) throws Exception {

		String ruta = Evidence.getStartEvidenceName() + "ArchExcel.xlsx"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
		ExcelFile excelFile = new ExcelFile(ruta, ExcelFile.CREATE);
		excelFile.createSheet("Destinos");
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
				if (paramUpper.contains("Codigo de Empresa"))
					dato = this.getCodigoTipoId(dato);
				else if ((paramUpper.contains("Referencia 1")))
					dato = catBancos.getCodigoXNombreContained(dato);
				else if ((paramUpper.contains("Referencia 2")))
					dato = this.getCodigoTipoProducto(dato);
				else if ((paramUpper.contains("Valor a pagar")))
					dato = this.getCodigoTipoProducto(dato);
				if (paramUpper.equals("Fecha límite"))
					dato = this.getCodigoTipoProducto(dato);

				excelFile.setStringCellValue(filaAD, posH + 1, dato);
			}
			filaAD++;
			this.totalDestinos++;
		}
		SettingsRun.getTestData().setParameter("rutaArchivo", ruta);
		excelFile.saveAndCloseFile();
		return ruta;
	}
// ===========================================[crearArchivoExcel_NomProv]===========================================================================

	// String archivoDatos = "C:\\Users\\pvega2\\OneDrive - DXC
	// Production\\Desktop\\Data\\01 Destinos PagosNóm-Prov.xls";
	private String crearArchivoExcel_NomProv(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		String[] header = { "Tipo de identificación", "Numero de identificacion", "Nombres", "Apellidos",
				"Codigo del banco", "Tipo de producto o servicio", "Numero de producto o servicio",
				"Valor del pago o de la recarga" };

		String[] params = { "tipoId", "numId", "nombre", "apellido", "bancoDestino", "tipoProdDestino",
				"numProdDestino", "valorPago", "referencia" };

		return crearExcel(archivoDatos, listaFilasExcel, header, params);

	}

// ======================================================================================================================

// ===========================================[crearArchivoExcel_CtasAFC]===========================================================================

	private String crearArchivoExcel_CtasAFC(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		String[] header = { "Numero de cuenta AFC", "Titular", "Numero de Identificación", "Valor de aporte",
				"Valor cuota contingente" };
		String[] params = { "numeroAFC", "titular", "numId", "valorAporte", "valorCuotaCont" };

		return crearExcel(archivoDatos, listaFilasExcel, header, params);

	}

// ======================================================================================================================

// ===========================================[crearArchivoExcel_Cred3ros]===========================================================================

	private String crearArchivoExcel_Cred3ros(String archivoDatos, List<Integer> listaFilasExcel) throws Exception {

		String[] header = { "tipo de producto destino", "numProdDestino", "titular", "numDocumento", "valorPagar" };
		String[] params = { "tipoProdDestino", "numProdDestino", "titular", "numDocumento", "valorPagar" };

		return crearExcel(archivoDatos, listaFilasExcel, header, params);

	}

// ======================================================================================================================

// ===========================================[crearExcel]===========================================================================

	private String crearExcel(String archivoSource, List<Integer> listaFilasSourceExcel, String[] header,
			String[] paramsSource) throws Exception {

		String ruta = Evidence.getStartEvidenceName() + "ArchExcel.xlsx"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
		ExcelFile excelFile = new ExcelFile(ruta, ExcelFile.CREATE);
		excelFile.createSheet("Destinos");
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
				else if ((paramUpper.contains("TIPO") && paramUpper.contains("PROD")))
					dato = this.getCodigoTipoProducto(dato);
				if (paramUpper.equals("VALORPAGO") && !dato.isEmpty())
					this.valorTransaccion += Double.valueOf(dato);

				excelFile.setStringCellValue(filaAD, posH + 1, dato);
			}
			filaAD++;
			this.totalDestinos++;
		}
		excelFile.saveAndCloseFile();
		return ruta;
	}

// ======================================================================================================================

// ===========================================[getTipoCuenta]===========================================================================

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

// ======================================================================================================================

// ===========================================[CuentasMasivas]===========================================================================
	/**
	 * Obtiene los datos del archivoDest en DatosDesMasi
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] CuentasMasivas() throws Exception {
		DataDriven dd = new DataDriven(archivoDest);
		String[] DatosDesMasi = new String[listRowExcel.size()];
		// Crea un array para almacenar los datos extraídos
		int contador = 0;
		for (int fila : listRowExcel) {
			String tipoCarga = SettingsRun.getTestData().getParameter("Tipo de Carga").trim();
			if (!tipoCarga.equals("Archivo existente Plano") && !tipoCarga.equals("Archivo existente Excel")) {
				String tipoProdDestino = dd.getParameterByRow("tipoProdDestino", fila);
				String numProdDestino = dd.getParameterByRow("numProdDestino", fila);
				String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
				if (servicio.equals("Crédito.3ros")) {
					String valorPago = dd.getParameterByRow("valorPagar", fila);
					DatosDesMasi[contador] = tipoProdDestino + "|" + numProdDestino + "|";
				} else {
					String valorPago = dd.getParameterByRow("valorPago", fila);
					DatosDesMasi[contador] = tipoProdDestino + "|" + numProdDestino + "|" + valorPago;
				}
			} else {

			}
			contador++;
		}
		return DatosDesMasi;
	}

// ======================================================================================================================

// ===========================================[obtenerValorDeHoja]===========================================================================

	private static String obtenerValorDeHoja(Sheet hoja) {
		String resultado = null;

		for (Row row : hoja) {
			Cell cell = row.getCell(5); // Suponemos que la descripción está en la primera columna (columna 0)
			if (cell != null) {
				Cell mtvoCell = row.getCell(6); // Suponemos que el MTVO está en la segunda columna (columna 1)
				if (mtvoCell != null) {
					resultado = mtvoCell.getStringCellValue();
//						resultado = mtvoCell.getNumericCellValue();
				}
				break; // Salir del bucle si se encuentra la descripción
			}
		}

		return resultado;
	}

// ======================================================================================================================

// ===========================================[BuscarCuentasMigrada]===========================================================================
	public String BuscarCuentasMigrada(String tipoCuenta, String Cuenta) throws Exception {
		String filePath = "C:\\temp\\Tipos TX pyme.xls";
		try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			// Leer datos de la Hoja1
			if (tipoCuenta.contains("AHORROS") || tipoCuenta.contains("ahorros"))
				tipoCuenta = "ahorro";
			else if (tipoCuenta.contains("CORRIENTE") || tipoCuenta.contains("corriente")) // CRÉDIPLUS
				tipoCuenta = "corriente";
			String mtvo = obtenerValorDeHoja(workbook.getSheet("Hoja4"), tipoCuenta, Cuenta);
			// Leer datos de la Hoja2
			return mtvo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

// ======================================================================================================================

// ===========================================[obtenerValorDeHoja]===========================================================================

	private static String obtenerValorDeHoja(Sheet hoja, String tipoCuenta, String cuenta) {
		String resultado = null;

		for (Row row : hoja) {
			Cell tipocuenta = row.getCell(0); // Suponemos que la descripción está en la primera columna (columna 0)
			Cell cell1 = row.getCell(1); // Suponemos que la descripción está en la segunda columna (columna 1)

			String tipocuentadato = " ";
			String cuentaHomologaright = " ";
			String cuentaHomologaLeft = " ";

			// Verificar si la celda tipocuenta no es nula y si tiene valor
			if (tipocuenta != null && tipocuenta.getCellType() == CellType.STRING
					&& tipocuenta.getStringCellValue() != null) {
				tipocuentadato = tipocuenta.getStringCellValue();
			}

			// Verificar si la celda cell1 no es nula y si tiene valor
			if (cell1 != null && cell1.getCellType() == CellType.STRING && cell1.getStringCellValue() != null) {
				cuentaHomologaright = DXCUtil.right(cell1.getStringCellValue(), 4);
				cuentaHomologaLeft = DXCUtil.left(cell1.getStringCellValue(), 9);
			}

			// Comparar y verificar condiciones
			if (cell1 != null && tipocuentadato.contains(tipoCuenta)
					&& cuentaHomologaLeft.contains(DXCUtil.left(cuenta, 9))
					&& cuentaHomologaright.equals(DXCUtil.right(cuenta, 4))) {

				Cell mtvoCell = row.getCell(2); // Suponemos que el MTVO está en la tercera columna (columna 2)

				// Verificar si la celda mtvoCell no es nula y si tiene valor
				if (mtvoCell != null && mtvoCell.getCellType() == CellType.STRING) {
					resultado = mtvoCell.getStringCellValue();
				}
				break; // Salir del bucle si se encuentra la descripción
			}
		}

		return resultado;
	}

// ======================================================================================================================

// ===========================================[CrearArchivoPlano_Segmentos]===========================================================================

	/**
	 * Crea el archivo de Segmentos para pyme.
	 * 
	 * @param segmentosStratus
	 * @throws Exception
	 */
	public void CrearArchivoPlano_Segmentos(HashMap<String, Double> segmentosStratus) throws Exception {

		String ruta = SettingsRun.getParentResultDir() + "Archivo_Segmentos.txt"; // directorioEvidencia\yyyymmdd-HHmmss
																					// // nbFile
		File file = new File(ruta);
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		// Escribir los valores del HashMap en el archivo
		for (String key : segmentosStratus.keySet()) {
			bw.write(key + ": " + segmentosStratus.get(key));
			bw.newLine(); // Agregar nueva línea después de cada valor
		}
		bw.close();
	}
// ======================================================================================================================

// ===========================================[LeerArchivoPlanoSegmentos]===========================================================================

	/**
	 * Lee el archivo plano generado por la automatizacion de los segmentos
	 * 
	 * @return retorna los Keys y conceptos
	 * @throws Exception
	 */
	public HashMap<String, Double> LeerArchivoPlano() throws Exception {
		HashMap<String, Double> segmentosStratus = new HashMap<String, Double>();
		File fr = new File(SettingsRun.getParentResultDir() + "Archivo_Segmentos.txt");
		BufferedReader leerArch = new BufferedReader(new FileReader(fr));
		String linea;
		while ((linea = leerArch.readLine()) != null) {
			// Dividir la línea en clave y valor
			String[] partes = linea.split(": ");
			if (partes.length == 2) {
				String clave = partes[0].trim();
				Double valor = Double.parseDouble(partes[1].trim());
				segmentosStratus.put(clave, valor);
			}
		}
		leerArch.close();
		Reporter.reportEvent(Reporter.MIC_INFO, "Se obtine datos de Segmento datos Pyme desde Archivo: "
				+ SettingsRun.getParentResultDir() + "Archivo_Segmentos.txt");
		return segmentosStratus;
	}

// ======================================================================================================================

// ============================================[Leer datos archivo Libranza]===========================================================================================

	public String[] BuscarDatosLibranza(String rutaArchivoCarga) throws Exception {
		String resultado = null;
		DataDriven dd = new DataDriven(rutaArchivoCarga, "Descarga");
		int contador2 = dd.getLastRow() + 1;
		int contador = 0;
		String[] DatosDesMasi = new String[contador2 - 2];
		// Crea un array para almacenar los datos extraídos

		for (int i = 2; i < (contador2); i++) {
			String estado = dd.getParameterByRow("Estado", i);
			String Observaciones = dd.getParameterByRow("Observaciones", i);
			if (Observaciones == "") {
				Observaciones = " ";
			}
			DatosDesMasi[contador] = estado + "|" + Observaciones;
			contador++;

		}
		return DatosDesMasi;
	}

	public boolean ReferenciasEcxel1(String rutaArchivocarga) throws Exception {

		boolean Archivo = false;
		String filasATomar = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();

		if (filasATomar.equals("N/A")) {
			archivoCargar = Evidence.saveFile(archivoDest);
		} else {
			listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		}

		List<Integer> listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		String resultado = null;
		DataDriven dd = new DataDriven(rutaArchivocarga, "Data");
//		int contador2 = dd.getLastRow() + 1;
		int contador = 0;
		String[] datosDesMasi = new String[listRowExcel.size()];
		// Crea un array para almacenar los datos extraídos

		for (int fila : listRowExcel) {
			String CodigoEmpresa = dd.getParameterByRow("Codigo de Empresa", fila);
			String Referencia1 = dd.getParameterByRow("Referencia 1", fila);
			String valorPago = dd.getParameterByRow("Valor a pagar", fila);
			String FechaLimite = dd.getParameterByRow("Fecha límite", fila);
			datosDesMasi[contador] = CodigoEmpresa + "|" + Referencia1 + "|" + valorPago + "|" + FechaLimite;
			contador++;

		}
		this.crearArchivoExcel_Referencias1Recaudos(rutaArchivocarga, listRowExcel);
		Archivo = true;
		return Archivo;

	}

	/**
	 * 
	 * @param rutaArchivocarga
	 * @return
	 * @throws Exception
	 */
	public boolean ReferenciasEcxel2(String rutaArchivocarga) throws Exception {

		boolean Archivo = false;
		String filasATomar = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();

		if (filasATomar.equals("N/A")) {
			archivoCargar = Evidence.saveFile(archivoDest);
		} else {
			listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		}

		List<Integer> listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		String resultado = null;
		DataDriven dd = new DataDriven(rutaArchivocarga, "Data");
		int contador = 0;
		String[] DatosDesMasi = new String[listRowExcel.size()];
		// Crea un array para almacenar los datos extraídos

		for (int fila : listRowExcel) {
			String CodigoEmpresa = dd.getParameterByRow("Codigo de Empresa", fila);
			String Referencia1 = dd.getParameterByRow("Referencia 1", fila);
			String Referencia2 = dd.getParameterByRow("Referencia 2", fila);
			String valorPago = dd.getParameterByRow("Valor a pagar", fila);
			String FechaLimite = dd.getParameterByRow("Fecha límite", fila);
			DatosDesMasi[contador] = CodigoEmpresa + "|" + Referencia1 + "|" + Referencia2 + "|" + valorPago + "|"
					+ FechaLimite;
			contador++;
		}

		this.crearArchivoExcel_Referencias2Recaudos(rutaArchivocarga, listRowExcel);
		Archivo = true;
		return Archivo;

	}

	public boolean ReferenciasArchivoPlano130_50(String rutaArchivocarga) throws Exception {

		boolean Archivo = false;
		String filasATomar = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();

		if (filasATomar.equals("N/A")) {
			archivoCargar = Evidence.saveFile(archivoDest);
		} else {
			listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		}

		List<Integer> listRowExcel = DXCUtil.getListaDatos(filasATomar, ",");
		String resultado = null;
		DataDriven dd = new DataDriven(rutaArchivocarga, "Data");
//	int contador2 = dd.getLastRow() + 1;
		int contador = 0;
//	String[] DatosDesMasi = new String[500];
		String[] DatosDesMasi = new String[listRowExcel.size()];
		// Crea un array para almacenar los datos extraídos

		for (int fila : listRowExcel) {
			String Txt = dd.getParameterByRow("Referenicas en formato TXT", fila);
			DatosDesMasi[contador] = Txt;
//		System.out.println(DatosDesMasi[contador]);
			contador++;

		}
		this.crearArchivoPlano_Insertar(DatosDesMasi);
		Archivo = true;
		return Archivo;

	}

	private boolean crearArchivoPlano_Insertar(String[] dato) throws Exception {
		boolean Archivo = false;
		if (dato.equals("N/A")) {
			archivoCargar = Evidence.saveFile(archivoDest);
		} else {

			ruta = Evidence.getStartEvidenceName() + "ArchPlano.txt"; // directorioEvidencia\yyyymmdd-HHmmss nbFile
			File file = new File(ruta);
			if (!file.exists())
				file.createNewFile();

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String datorecore : dato) {
				bw.write(datorecore + "\n");
			}
			bw.close();
			SettingsRun.getTestData().setParameter("rutaArchivo", ruta);
			Archivo = true;

		}
		return Archivo;
	}

}
// ==================================================================================================================
