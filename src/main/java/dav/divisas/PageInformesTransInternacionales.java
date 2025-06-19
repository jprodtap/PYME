package dav.divisas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Calendar;
import dav.pymes.PageLoginPymes;


import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageInformesTransInternacionales extends PageDivisas {

	public PageInformesTransInternacionales(PageLoginPymes parentPage) {
		super(parentPage);
	}

	// Botones de seleccion Transferencia internacionales o Accesos al sistema
	WebElement linkTransferenciasInternacionales;
	WebElement linkAccesosAlSistema;
	WebElement linkVerDetalles;

	// Se extrae los array de la transaccion y consultas para comprara
	static String numAprova = null;
	static String[] dataArrayConsultas;
	static String[] dataComprobanteConsultas;

	String numAprobacion = null;
	String today = "";


	By link = By.xpath("//li[@onclick][1]");
	By linkAcceAlSistema = By.xpath("//li[@onclick][2]");

	// Campos y tablas con las que se va a interactuar
	By fechaDesde = By.xpath("//*[@id='FechaDesde']");
	By fechaHasta = By.xpath("//*[@id='FechaHasta']");

	By table = By.xpath("//*[@class='table  table-responsive table-condensed table-hover table-bordered mt10']");
	By tableVerDetalles = By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/table/tbody/tr[2]/td/table[1]");
	By tableVerDetalles2 = By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/table/tbody/tr[2]/td/table[2]");
	By tableVerDetalles3 = By.xpath("/html/body/div[2]/div[1]/div[3]/div[2]/table");

	By btnBuscar = By.xpath("//*[@id='btnBuscar']");
	By btnBuscarAccesos = By.xpath("//*[@id='paginacion']/div[1]/div[7]/button");
	By masPaginas = By.xpath("//*[@id='pagina']");
	By option = By.xpath("//*[@id='pagina']/option");

	String datoRegistros = "//*[@id='paginacion']/div[2]/div[3]/table/tbody/tr[I]/td[12][contains(text(),'NoAprobacion')]";

	String datoRegistrosTabla = "//*[@id='paginacion']/div[2]/div[3]/table/tbody/tr[I]/td[J]";
	String datoRegistrosheader = "//*[@id='paginacion']/div[2]/div[3]/table/thead/tr/th[J]/button";

	// Validaciones necesarias para el set de informes
	By Alerta = By.xpath("//*[@id='AlertaModal']/div/div/div[2]/p");
	By tablaPCT = By.xpath("//*[@id='paginacion']/div[3]/div/table/tbody");

	By btnImprimir = By.xpath("//*[@id='paginacion']/div[4]/div/button[1]");
	By btnDescargar = By.xpath("//*[@id='paginacion']/div[4]/div/button[2]");

	By subResultado = By.xpath("//*[@id='paginacion']/div/div[1]/div/h3[contains (text(), 'Resultado de la b�squeda')]");

	// Variables para busqueda TI o AS
	String cantidadPaginas = "//*[@id='pagina']/option[NUM_PAGINA]";

	String verDetalles;

	String datoRegistrosporFecha = "//*[@id='paginacion']/div[2]/div[2]/table/tbody/tr[I]/td[6][contains(text(),'FechaHora')]";
	String verDetalleTx = "//*[@id='paginacion']/div[2]/div[2]/table/tbody/tr[I]/td[9]/a";

	String acumuladorTable;

	String tablaValidad = "//*[@id='paginacion']/div[2]/div[2]/table/tbody/tr[I]/td[J]";

	String headerValidad = "//*[@id='paginacion']/div[2]/div[2]/table/thead/tr/th[J]";
	String headerValidadbutton = "//*[@id='paginacion']/div[2]/div[2]/table/thead/tr/th[J]/button";

	String[][] RegistroTI;
	String[][] RegistroAS;

	String fechaHora;
	String fechaCampo;

	int cantidadDeOptions;
	int numPagina;
	int i;
	boolean encontradoTI;
	boolean encontradoAS;
	boolean primeraBusqueda = true;

// ***********************************************************************************************************************
	/**
	 * Estos metodos obtienen los array de datos de la transaccion y consultas para
	 * comparar con los datos de informes
	 */
	public void dataInformes(String[] filaArrayConsultas) {

		dataArrayConsultas = new String[8];
		dataArrayConsultas = filaArrayConsultas;

	}

	public void dataComprobanteInformes(String[] filaComprobanteConsultas) {

		dataComprobanteConsultas = new String[20];
		dataComprobanteConsultas = filaComprobanteConsultas;

	}

//***********************************************************************************************************************
	/**
	 * Metodo inicial, ejecuta los metodos de todo el flujo de informes organizados
	 */
	public void InformeTransInter() throws Exception {


		fechaHora = dataArrayConsultas[0];
		String NoAprobacion = dataArrayConsultas[1];
		String fecha = dataArrayConsultas[0];

		fechaCampo = fecha;

		fecha = fecha.substring(0, fecha.length() - 9);
		fechaHora = fechaHora.substring(0, fechaHora.length() - 3);

		Date fechaTxTemp = new Date();

		today = DXCUtil.dateToString(fechaTxTemp, "dd/mm/yyyy");

		// Selecciona el modulo de Informes Transferencia Internacionales
		this.irAOpcion("", "Informes", "Informes Transferencias Internacionales");
		
		DXCUtil.wait(2);

		// Cambiar el contexto del driver al iframe
		this.switchToFrameDivisas();
		Reporter.write(" ");
		Reporter.write("==========[INFORMES - TRANSFERENCIAS INTERNACIONALES]======================================================================================================= ");
		Reporter.write(" ");

		linkTransferenciasInternacionales = this.getDriver().findElement(link);
		this.TransferenciasInternacionales(fecha, NoAprobacion);

		Reporter.write(" ");
		Reporter.write("==========[INFORMES - ACCESOS AL SISTEMA]======================================================================================================= ");
		Reporter.write(" ");
		

		linkAccesosAlSistema = this.getDriver().findElement(linkAcceAlSistema);
		String msg = this.AccesosAlSistema(fecha, fechaHora);

		if (msg != null) {

			String newDateString = this.subtractOneMinute(fechaHora);
			fechaHora = newDateString;

			linkAccesosAlSistema = this.getDriver().findElement(linkAcceAlSistema);
			msg = this.AccesosAlSistema(fechaCampo, newDateString);

		}

		if (msg != null) {

			String newDateString = this.subtractOneMinute(fechaHora);

			linkAccesosAlSistema = this.getDriver().findElement(linkAcceAlSistema);
			msg = this.AccesosAlSistema(fechaCampo, newDateString);

			if (msg != null) {
				this.pageLogin.CerrarSesionMiddle();
				SettingsRun.exitTestIteration();
			}

		}

		linkVerDetalles = this.getDriver().findElement(By.xpath(verDetalles));
		linkVerDetalles.click();

		DXCUtil.wait(3);

		Evidence.saveAllScreens("Informes_ver detalles", this);
		Reporter.write(" ");
		Reporter.write("==========[INFORMES - VER DETALLES]======================================================================================================= ");
		Reporter.write(" ");

		// Imprime en el LOG todo el comprobante de "Ver Detalles", organizado en saltos de linea - 1ra tabla
		WebElement tabla = this.findElement(tableVerDetalles);
		java.util.List<WebElement> filas = tabla.findElements(By.tagName("tr"));

		for (WebElement fila : filas) {
			acumuladorTable = "";
			i = 0;
			java.util.List<WebElement> columnas = fila.findElements(By.tagName("td"));
			for (WebElement columna : columnas) {
				i++;
				acumuladorTable = acumuladorTable + columna.getText() + "\t";
				if (i == 2) {
					acumuladorTable = acumuladorTable + "\n" + "*** INFO ----------- ";
				}
			}

			if (!acumuladorTable.equals("*** INFO ----------- ")) {
				
				Reporter.reportEvent(Reporter.MIC_INFO, acumuladorTable);
			}
		}

		// Imprime en el LOG todo el comprobante de "Ver Detalles", organizado en saltos  de linea - 2da tabla
		WebElement tabla2 = this.findElement(tableVerDetalles2);
		java.util.List<WebElement> filas2 = tabla2.findElements(By.tagName("tr"));

		for (WebElement fila : filas2) {
			acumuladorTable = "";
			i = 0;
			java.util.List<WebElement> columnas2 = fila.findElements(By.tagName("td"));
			for (WebElement columna : columnas2) {
				i++;
				acumuladorTable = acumuladorTable + columna.getText() + "\t";
				if (i == 2) {
					acumuladorTable = acumuladorTable + "\n" + "*** INFO ----------- ";
				}
			}
			
			 if (!acumuladorTable.equals("*** INFO ----------- ")) {
				
				 Reporter.reportEvent(Reporter.MIC_INFO, acumuladorTable);
			}
		}


		// Imprime en el LOG todo el comprobante de "Ver Detalles", organizado en saltos de linea - 3ra tabla
		WebElement tabla3 = this.findElement(tableVerDetalles3);
		java.util.List<WebElement> filas3 = tabla3.findElements(By.tagName("tr"));

		for (WebElement fila : filas3) {
			acumuladorTable = "";
			i = 0;
			java.util.List<WebElement> columnas3 = fila.findElements(By.tagName("td"));
			for (WebElement columna : columnas3) {
				i++;
				acumuladorTable = acumuladorTable + columna.getText() + "\t";
			}
			
			Reporter.reportEvent(Reporter.MIC_INFO, acumuladorTable);
		}
	}

//***********************************************************************************************************************
	
	/**
	 * Realiza el flujo de la ventana Tranferencias Internacionales
	 */
	public void TransferenciasInternacionales(String today, String NoAprobacion) throws Exception {

		linkTransferenciasInternacionales.click();

		do {
			DXCUtil.wait(1);
		} while (this.element(btnBuscar) == null);

		this.element(fechaDesde).sendKeys(today);
		this.element(fechaHasta).sendKeys(today);

		DXCUtil.wait(1);

		this.click(btnBuscar);

		int i = 0;
		do {
			DXCUtil.wait(1);
			i++;
		} while (this.element(Alerta) == null && i < 10);

		if (this.element(Alerta) != null) {
			String msj = this.closeActiveIntAlert();
			Reporter.reportEvent(Reporter.MIC_FAIL, "Error en Informes Tranferencias Internacionales:  " + msj);
			this.pageLogin.CerrarSesionMiddle();
			SettingsRun.exitTestIteration();
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(table) == null);

		if (this.element(subResultado) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS, "Aparece el subtitulo 'Resultado de la búsqueda'");
			
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No aparece el subtitulo 'Resultado de la búsqueda'");
		}

		if (this.element(tablaPCT) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS,
					"Aparece la table de informacion paginacion, contador y total COP en Transferencias Internacionales");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"No aparece la table de informacion paginacion, contador y total COP en Transferencias Internacionales");
		}

		if (this.element(btnImprimir) != null && this.element(btnDescargar) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS,
					"Aparece el boton de descargar e imprimir en Transferencia Internacionales");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"No parece los botone de descargar y/o imprimir en Transferencias Internacionales");
		}

		Evidence.saveAllScreens("Informes - transferencias internacionales", this);

		if (this.element(masPaginas) != null) {

			cantidadDeOptions = 0;
			numPagina = 1;
			i = 0;
			encontradoTI = false;

			do {

				RegistroTI = this.SearchAndSave_TransferenciasInternacionales(NoAprobacion);

				if (numPagina == cantidadDeOptions && encontradoTI == false) {
					Reporter.reportEvent(Reporter.MIC_FAIL,
							"No se escuentra el registro con numero de aprobacion:  " + NoAprobacion);
					numPagina++;
					this.pageLogin.CerrarSesionMiddle();
					SettingsRun.exitTestIteration();
				}

			} while (numPagina <= cantidadDeOptions && encontradoTI == false);

		} else {
			i = 0;
			RegistroTI = this.SearchAndSave_TransferenciasInternacionales(NoAprobacion);
		}

		this.Comparacion_TransferenciasInternacionales(RegistroTI[0], RegistroTI[1]);

	}

//***********************************************************************************************************************
	/**
	 * Realiza el flujo de la ventana Accesos al Sistema
	 */
	public String AccesosAlSistema(String today, String fechaHora) throws Exception {

		linkAccesosAlSistema.click();

		DXCUtil.wait(5);

		do {
		} while (this.element(btnBuscarAccesos) == null);

		this.element(fechaDesde).sendKeys(today);
		this.element(fechaHasta).sendKeys(today);

		DXCUtil.wait(3);

		this.click(btnBuscarAccesos);

		int i = 0;
		do {
			DXCUtil.wait(1);
			i++;
		} while (this.element(Alerta) == null && i < 10);

		if (this.element(Alerta) != null) {
			String msj = this.closeActiveIntAlert();
			Reporter.reportEvent(Reporter.MIC_FAIL, "Error en Informes Acceso al Sistema:  " + msj);
			this.pageLogin.CerrarSesionMiddle();
			SettingsRun.exitTestIteration();
		}

		do {
		} while (this.element(table) == null);

		if (this.element(subResultado) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS, "Aparece el subtitulo 'Resultado de la búsqueda'");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No aparece el subtitulo 'Resultado de la búsqueda'\"");
		}

		if (this.element(tablaPCT) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS,
					"Aparece la table de informacion paginacion y contador en Accesos al Sistema");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"No aparece la table de informacion paginacion y contador en Accesos al Sistema");
		}

		if (this.element(btnImprimir) != null && this.element(btnDescargar) != null) {

			Reporter.reportEvent(Reporter.MIC_PASS, "Aparece el boton de descargar e imprimir en Accesos al Sistema");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"No parece los botone de descargar y/o imprimir en Accesos al Sistema");
		}

		Evidence.saveAllScreens("Informes - accesos al sistema", this);

		if (this.element(masPaginas) != null) {

			cantidadDeOptions = 0;
			numPagina = 1;
			i = 0;
			encontradoTI = false;

			do {

				RegistroAS = this.SearchAndSave_AccesosAlSistema(fechaHora);

				if (numPagina == cantidadDeOptions && encontradoAS == false) {
					Reporter.reportEvent(Reporter.MIC_FAIL,
							"No se escuentra el registro con fecha y hora de aprobacion:  " + fechaHora);
					numPagina++;

					return "No se escuentra el registro";
				}

			} while (numPagina <= cantidadDeOptions && encontradoAS == false);

		} else {

			i = 0;
			RegistroAS = this.SearchAndSave_AccesosAlSistema(fechaHora);

			if (RegistroAS == null) {
				return "No se escuentra el registro";
			}

		}

		this.Comparacion_AccesosAlSistema(RegistroAS[0], RegistroAS[1]);
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Este metodo busca el registro por numero de aprobacion Almacena en una matriz
	 * los datos de llamados de la transaccion vs los datos del informe para la
	 * ventana de transferencia internacionales
	 */
	public String[][] SearchAndSave_TransferenciasInternacionales(String NoAprobacion) throws Exception {

		String registroEncontrado[] = new String[16];
		String headerRegistro[] = new String[16];

		if (this.element(masPaginas) != null) {
			WebElement elementosPagina = this.findElement(masPaginas);
			List<WebElement> opciones = elementosPagina.findElements(option);
			cantidadDeOptions = opciones.size();
		}

		DXCUtil.wait(4);

		i = 0;
		do {
			i++;

			String datoRegistro = datoRegistros.replace("I", String.valueOf(i)).replace("NoAprobacion", NoAprobacion);
			DXCUtil.wait(1);

			if (this.element(datoRegistro) != null) {

				int j = 1;
				do {

					registroEncontrado[j - 1] = this.element(datoRegistrosTabla.replace("I", String.valueOf(i)).replace("J", String.valueOf(j))).getText();

					headerRegistro[j - 1] = this.element(datoRegistrosheader.replace("J", String.valueOf(j))).getText();

					j++;

				} while (j < 16);
				
				encontradoTI = true;
			}

			if (i == 20 && numPagina < cantidadDeOptions && this.element(masPaginas) != null) {

				numPagina++;
				String xPathPagina = cantidadPaginas.replace("NUM_PAGINA", String.valueOf(numPagina));
				this.click(By.xpath(xPathPagina));
				DXCUtil.wait(2);
				i = 0;

			}

			if (i == 20 && encontradoTI == false && this.element(masPaginas) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL,"No se escuentra el registro con numero de aprobacion:  " + NoAprobacion);
				this.pageLogin.CerrarSesionMiddle();
				SettingsRun.exitTestIteration();
			}

		} while (this.element(btnBuscar) != null && i < 20 && encontradoTI == false);

		String[][] Registro = { headerRegistro, registroEncontrado };
		
		return Registro;

	}

//***********************************************************************************************************************
	/**
	 * Este metodo busca el registro por fecha y hora de transaccion Almacena en una
	 * matriz los datos de llamados de la transaccion vs los datos del informe para
	 * la ventana de accesos al sistema
	 */
	public String[][] SearchAndSave_AccesosAlSistema(String fechaHora) throws Exception {

		String registroEncontradoAcc[] = new String[9];
		String headerRegistroAcc[] = new String[9];

		if (this.element(masPaginas) != null) {

			WebElement elementosPagina = this.findElement(masPaginas);
			List<WebElement> opciones = elementosPagina.findElements(option);
			cantidadDeOptions = opciones.size();
		}

		i = 0;
		do {
			DXCUtil.wait(1);
			i++;

			String datoRegistro = datoRegistrosporFecha.replace("I", String.valueOf(i)).replace("FechaHora", fechaHora);

			verDetalles = verDetalleTx.replace("I", String.valueOf(i));

			if (this.element(datoRegistro) != null) {

				int j = 1;
				do {

					registroEncontradoAcc[j - 1] = this
							.element(tablaValidad.replace("I", String.valueOf(i)).replace("J", String.valueOf(j)))
							.getText();

					if (j == 4 || j == 9) {

						headerRegistroAcc[j - 1] = this.element(headerValidad.replace("J", String.valueOf(j))).getText();
					} else {

						headerRegistroAcc[j - 1] = this.element(headerValidadbutton.replace("J", String.valueOf(j))).getText();
					}

					j++;

				} while (j < 10);
				encontradoAS = true;
			}

			if (i == 20 && numPagina < cantidadDeOptions && this.element(masPaginas) != null) {

				numPagina++;
				String xPathPagina = cantidadPaginas.replace("NUM_PAGINA", String.valueOf(numPagina));
				this.click(By.xpath(xPathPagina));
				DXCUtil.wait(2);
				i = 0;
			}

			if (i == 20 && encontradoAS == false && this.element(masPaginas) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL,"No se escuentra el registro con fecha y hora de aprobacion:  " + fechaHora);

				return null;
			}

		} while (this.element(btnBuscarAccesos) != null && i < 20 && encontradoAS == false);

		String[][] Registro = { headerRegistroAcc, registroEncontradoAcc };
		return Registro;
	}

//***********************************************************************************************************************
	/**
	 * Realiza la comparacion de datos de la transaccion vs datos del informe en
	 * tranferencias internacionales
	 */
	public void Comparacion_TransferenciasInternacionales(String[] headerRegistro, String[] bodyRegistro) throws Exception {

		String numIdEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		String tipoTx = SettingsRun.getTestData().getParameter("Servicio").trim();
		String numeral2 = SettingsRun.getTestData().getParameter("Numeral cambiario 2").trim();

		dataArrayConsultas[0] = dataArrayConsultas[0].substring(11);

		this.ValidarIgualdad(headerRegistro[0], bodyRegistro[0], dataArrayConsultas[2].toUpperCase());
		this.ValidarIgualdad(headerRegistro[1], bodyRegistro[1], today);
		this.ValidarIgualdad(headerRegistro[2], bodyRegistro[2], dataArrayConsultas[0]);

		i = 2;
		do {

			DXCUtil.wait(1);
			i++;

			this.ValidarIgualdad(headerRegistro[i], bodyRegistro[i], dataArrayConsultas[i]);

		} while (i < 6);

		if (tipoTx.contains("Enviar")) {
			if (numeral2.equals("")) {
				this.ValidarIgualdad(headerRegistro[7], bodyRegistro[7], dataComprobanteConsultas[13]);
			} else {
				this.ValidarIgualdad(headerRegistro[7], bodyRegistro[7], dataComprobanteConsultas[14]);
			}
		} else {
			this.ValidarIgualdad(headerRegistro[7], bodyRegistro[7], dataComprobanteConsultas[12]);
		}

		this.ValidarIgualdad(headerRegistro[8], bodyRegistro[8], dataArrayConsultas[7].toUpperCase());
		this.ValidarIgualdad(headerRegistro[9], bodyRegistro[9], numIdEmpresa);

		if (tipoTx.contains("Enviar")) {
			if (bodyRegistro[10].contains(dataComprobanteConsultas[3])) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en " + headerRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_PASS, bodyRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_PASS, dataComprobanteConsultas[3]);

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en " + headerRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_FAIL, bodyRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobanteConsultas[3]);
			}

		} else {
			if (bodyRegistro[10].contains(dataComprobanteConsultas[6])) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en " + headerRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_PASS, bodyRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_PASS, dataComprobanteConsultas[6]);

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en " + headerRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_FAIL, bodyRegistro[10]);
				Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobanteConsultas[6]);
			}
		}

		this.ValidarIgualdad(headerRegistro[11], bodyRegistro[11], dataArrayConsultas[1]);

	}

//***********************************************************************************************************************
	/**
	 * Realiza la comparacion de datos de la transaccion vs datos del informe en
	 * accesos al sistema
	 */
	public void Comparacion_AccesosAlSistema(String[] headerRegistro, String[] bodyRegistro) throws Exception {

		String tipoTx = SettingsRun.getTestData().getParameter("Servicio").trim();
		String tipoId = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String idUsuario = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String servicio = "Transferencias Internacionales";

		bodyRegistro[5] = bodyRegistro[5].substring(0, bodyRegistro[5].length() - 3);

		this.ValidarIgualdad(headerRegistro[0], bodyRegistro[0], dataArrayConsultas[4]);
		this.ValidarIgualdad(headerRegistro[1], bodyRegistro[1].toUpperCase(), tipoId);
		this.ValidarIgualdad(headerRegistro[2], bodyRegistro[2], idUsuario);
		this.ValidarIgualdad(headerRegistro[3], bodyRegistro[3], servicio);
		this.ValidarIgualdad(headerRegistro[4], bodyRegistro[4], dataComprobanteConsultas[1]);
		this.ValidarIgualdad(headerRegistro[5], bodyRegistro[5], fechaHora);

	}

//***********************************************************************************************************************
	/**
	 * Es un comparador de datos Imprime en el log los datos y el mensaje de
	 * coincidencia correcta o incorrecta
	 */
	public void ValidarIgualdad(String headerRegistro, String bodyRegistro, String dataConsulta) throws Exception {

		if (bodyRegistro.equals(dataConsulta)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en " + headerRegistro);
			Reporter.reportEvent(Reporter.MIC_PASS, bodyRegistro);
			Reporter.reportEvent(Reporter.MIC_PASS, dataConsulta);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en " + headerRegistro);
			Reporter.reportEvent(Reporter.MIC_FAIL, bodyRegistro);
			Reporter.reportEvent(Reporter.MIC_FAIL, dataConsulta);
		}
	}

	private String subtractOneMinute(String fechaHora) throws ParseException {

		SimpleDateFormat simplef = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = simplef.parse(fechaHora);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.add(Calendar.MINUTE, -1);
		Date newDate = calendar.getTime();
		String newDateString = simplef.format(newDate);

		DXCUtil.wait(3);

		return newDateString;
	}

}
