package dav.Libranza;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.pymes.moduloCrearTx.ControllerDestinosMasivos;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageLibranza extends BasePageWeb {

	/**
	 * Variables de otras clases para llamar algunos métodos que tienen alguna
	 * relación.
	 */

	PageOrigen pageOrigen = null;
	int cont = 0;
	/**
	 * Locators modulo Libranza
	 * 
	 */

	// Locators Solicitudes Pendientes
	String seleccionConvenio = "//*[@id='esteunsection']/form/div[2]/table//tr/td/a[contains(text(), 'convenio')]";
	// Los siguientes locators funcionan para los modulos solicitudes pendientes y
	// consulta historicos: locRangoFechaDesde, locRangoFechaHasta, tipoIDCliente,
	// estadoSolicitud, convenios, mostrarDatos, locators para selección de fecha en
	// calendario
	By locRangoFechaDesde = By.xpath("//*[@class='input-group']/input[@formcontrolname='dateFrom']");
	By locRangoFechaHasta = By.xpath("//*[@class='input-group']/input[@formcontrolname='dateTo']");
	By tipoIDCliente = By.id("tipoID");
	By numeroIdCliente = By.id("numeroIDClienteSP");
	By estadoSolicitud = By.id("tipoEstado");
	By convenios = By.xpath("//*[@formcontrolname='idAgreement']");
	By mostrarDatos = By.xpath("//*[@value='Mostrar Datos']");
	By frameLibranza = By.id("cphCuerpo_frameLBZA");
	// locators selección mes y año calendario
	By monthCalendar = By.xpath("//*[@class='d-block ngb-dp-navigation-select']/select[1]");
	By yearCalendar = By.xpath("//*[@class='d-block ngb-dp-navigation-select']/select[2]");
	// locators calendario Rango de Fechas Desde
	By abrirCalendarFechaDesde = By.xpath("//*[@formcontrolname='dateFrom']/following-sibling::button");
	// Locators calendario Rango de Fechas Hasta
	By abrirCalendarFechaHasta = By.xpath("//*[@formcontrolname='dateTo']/following-sibling::button");
	// Locator rango de fecha incorrecto
	By msjError = By.xpath("//*[@id='AutorizacionesLibranzaEmpresas']/ngb-modal-window/div/div");
	// locators tabla de resultados
	By tabResultados = By.xpath("//*[@id='tableProcess']/tbody[1]"); // *[@id='tableProcess']
	By tablaResultPendSinInfo = By.xpath("//td[text()='No hay registros para esta consulta.']");
	By tituloColumnas = By.xpath("//*[@id='tableProcess']/thead/tr");
	String checkBoxXpath = "//*[text()='numIdCliente']/preceding::td/input[@id='row-checkBox']"; // preceding-sibling::td[contains(text(),'numIdCliente')]//preceding-sibling::td//input
	By inputObservacion = By.xpath("//*[@id=\"observacionesModal\"]/form/div[2]/input");
	By btnGuardar = By.xpath("//*[@id=\"observacionesModal\"]/form/div[3]/div/input");

	// locator descarga/carga solicitudes pendientes de aprobar
	By descargar = By.xpath("//*[@value=\"Descargar\"]");
	By formatoDescargas = By.xpath("(//*[@formcontrolname='tipoArch'])[1]");
	By formatoDescargaCargue = By.xpath("(//*[@formcontrolname='tipoArch'])[2]");
	By cargar = By.xpath("//*[@value='Cargar']");
	By inpRutaDelArchivo = By.xpath("//*[@class='ng-untouched ng-pristine ng-valid']/input[@type='file']");
	By btnCargarArchivo = By.xpath("//*[@value='Cargar archivo']");
	// locator para seleccionar checkbox en caso que solo se tenga una fila en la
	// tabla
	String seleccionXpathUnicaFila = "//*[text()='numIdCliente']/preceding::td/input"; // //
	// //*[text()='DOCUMENTO']/preceding::td/input
	By msjErrorCheckBox = By.xpath("//*[@id='AutorizacionesLibranzaEmpresas']/ngb-modal-window/div/div");
	By salarioMensual = By.xpath("//*[@id='tableProcess']/thead/tr/th[15]");
	By aprobadores = By.xpath("//*[@id='tableProcess']/thead/tr/th[19]");
	// aceptar credito de libranza
	By aceptar = By.xpath("//*[@class='modal-content']/div//div/input[@value='Aceptar']");
	By aceptarConfirm = By.xpath("//*[@class='modal-content']/div//input[@value='Aceptar']");
	By validarMsj = By.xpath("//*[@id='AutorizacionesLibranzaEmpresas']/ngb-modal-window/div/div");
	By Menu = By.id("mnMenu");

	// variable utilizada en el controller para manejar escenarios exitosos o
	// fallidos
	String msjInfo = "";

	// Locators Consulta Historicos

	By moduloConsultaHist = By.xpath("//*[@id='nav']/li[2]/a");
	By numclienteEmp = By.xpath("//*[@name='numeroIDCliente']");
	By tablaResultConsulHisSinInfo = By.xpath("(//td[text()='Sin registros para mostrar.'])[2]");
	By estadoSol = By.id("tipoEstado");
	String consultaHistFilaXpath = "//*[text()='numeroSolicitud']/following-sibling::td/ancestor::tr";
	By nombreColumnasTabla = By.xpath("//*[@id=\"tableHistorics\"]/thead/tr");
	By tipoContratoHis = By.xpath("//*[@id=\"tableHistorics\"]/thead/tr/th[15]");
	By respuestaBancoHis = By.xpath("//*[@id=\"tableHistorics\"]/thead/tr/th[21]");
	By msjErrorCargue = By.xpath("(//*[@class='text-center'])[1]");
	By iframeCargue = By.id("cphCuerpo_frameLBZA");

	By btnSiguiente = By.xpath("//*[@class='paginate_button next']");
	By btnPrimero = By.xpath("//*[@class='paginate_button first']");

	static WebElement[] infoFilaCheckBox = null;
	static String[] indiceCheckBox = null;
	static String[] arrayDatosFila = null; // 'dia')])[1]"

	static String[] estadoArchivoCargue = null;
	ControllerDestinosMasivos controllerDestinosMasivos = null;

	static String valoresFilas = "";

	public PageLibranza(BasePageWeb parentPage) {
		super(parentPage);
		//
	}

	/**
	 * 
	 * @param solicitudNumero       Se utiliza para flujos de 2 firmas o más, en el
	 *                              flujo con la 1 firma se extrae de la columna
	 *                              número de solicitud en el modulo de solicitudes
	 *                              pendientes
	 * @param datosFilasComparacion Contiene la información de las solicitudes de
	 *                              crédito de libranza que se aprueban, esta
	 *                              información se usa para verificar si la
	 *                              información coincide con la de consulta
	 *                              históricos
	 * @return Retorna array con la información de las solicitudes aprobadas o
	 *         mensaje de error si se presenta
	 */
	public String[] AprobarLibranza(String[] solicitudNumero, String[] datosFilasComparacion) throws Exception {
//		this.changeToDefaultFrame();
		// variables para obtener la información de la prueba a realizar en el excel
		String rangoFechaDesde = SettingsRun.getTestData().getParameter("Rango Fecha Desde");
		String rangoFechaHasta = SettingsRun.getTestData().getParameter("Rango Fecha Hasta");
		String tipoIdCliente = SettingsRun.getTestData().getParameter("Tipo ID Cliente");
		String numIdCliente = SettingsRun.getTestData().getParameter("Número ID Cliente");
		String estadoSolicitud = SettingsRun.getTestData().getParameter("Estado de solicitud");
		String convenios = SettingsRun.getTestData().getParameter("Convenio");
		String filtroBusqSolicPendiente = SettingsRun.getTestData().getParameter("Filtro de busqueda Solicitudes Pendientes");
		String cuotaMontoInfer = SettingsRun.getTestData().getParameter("Cuota monto inferior");
		String fechaIngresoEmp = SettingsRun.getTestData().getParameter("Fecha de ingreso");
		String tipoContrato = SettingsRun.getTestData().getParameter("Tipo de contrato");
		String observaciones = SettingsRun.getTestData().getParameter("Observaciones");
		String nroCreditosAprobaciones = SettingsRun.getTestData().getParameter("Número de creditos a aprobar");
		String salarioMensualEmple = SettingsRun.getTestData().getParameter("Salario Mensual");
		String tipoDePrueba = SettingsRun.getTestData().getParameter("Tipo de Prueba");
		String formatoDescarga = SettingsRun.getTestData().getParameter("Formato de descarga");
		String cargueDelArchivo = SettingsRun.getTestData().getParameter("Cargar desde archivo - Formato del archivo");
		String numeroAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones");
		String urlCargueArchivoExcel = SettingsRun.getTestData().getParameter("URL Cargue Archivo");

		// variable para encontrar el elemento dia en calendario por medio de un xPath

		String dayElementXpathSolPend = "(//*[@class='ngb-dp-month d-block']//div/div/div[(text()= 'dia'  and (not(contains(@class,'text-muted outside'))))])[1]"; // "(//*[@class='ngb-dp-month
																																									// d-block']//div/div/div[(text()=

		this.pageOrigen = new PageOrigen(this);
		String[] msj = new String[1];
		String[] observacionesArchivoCargue = null;

		do {
			DXCUtil.wait(1);

		} while (this.element(Menu) == null && cont <= 24);

		msj[0] = this.pageOrigen.irAOpcion("", "Gestión Recursos Humanos", "Libranza", "Autorizaciones Libranza");

		if (msj[0] != null) {
			return msj;
		}

		WebElement iframe = null;
		do {
			iframe = this.element(frameLibranza);
			DXCUtil.wait(1);
		} while (iframe == null);
		this.changeToFirstFrame();

		WebElement selconvenio = null;
		cont = 0;
		do {
			DXCUtil.wait(1);
			selconvenio = this.element(seleccionConvenio.replace("convenio", convenios));
			cont++;
		} while (selconvenio == null && cont <= 35);
		cont = 0;
		if (selconvenio != null) {
			this.click(selconvenio);
		} else {
			if (this.element(locRangoFechaDesde) == null) {
				msj[0] = "No fue posible ingresar al modulo de libranza, Verifique evidencia si se presentó falla de ambiente o no se encontró el convenio.";
				Evidence.save("No permite ingreso a modulo de libranza");
				return msj;
			} else {

			}

		}

		do {
			DXCUtil.wait(1);
			cont++;
		} while (this.element(locRangoFechaDesde) == null && cont < 5);
		if (this.element(locRangoFechaDesde) == null) {
			msj[0] = "No fue posible ingresar al modulo de libranza";
			Evidence.save("No permite ingreso a modulo de libranza");
			return msj;
		}

		Reporter.write("==============================[Solicitudes Pendientes]==============================");
		Reporter.write(" ");

		if (filtroBusqSolicPendiente.equals("Rango de Fechas")) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Parámetro de búsqueda: " + "[" + filtroBusqSolicPendiente + "]"
					+ " Rango Fecha Desde " + rangoFechaDesde + " Rango Fecha Hasta " + "[" + rangoFechaHasta + "]");
			SelectCalendarDate(rangoFechaDesde, abrirCalendarFechaDesde, monthCalendar, dayElementXpathSolPend,
					yearCalendar);
			SelectCalendarDate(rangoFechaHasta, abrirCalendarFechaHasta, monthCalendar, dayElementXpathSolPend,
					yearCalendar);
			this.click(mostrarDatos);
			cont = 0;
			do {
				DXCUtil.wait(1);
				cont++;
				if (this.element(msjError) != null) {
					String RangoIncorrecto = this.getText(msjError);
//					Evidence.save("Mensaje error", this);
					Evidence.save("Mensaje error");
					if (RangoIncorrecto.contains("El rango máximo permitido entre las fechas es de 30.")) {
						msj[0] = "Se visualiza el siguiente mensaje: El rango máximo permitido entre las fechas es de 30, Verifique rango de fechas";
						return msj;
					}
				}
			} while (this.element(tablaResultPendSinInfo) == null && cont < 4);

		} else if (filtroBusqSolicPendiente.equals("Tipo ID y Número ID")) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Parámetro de búsqueda: " + "[" + filtroBusqSolicPendiente + "]"
					+ " Tipo ID " + tipoIdCliente + " Número ID " + "[" + numIdCliente + "]");
			this.selectListItem(tipoIDCliente, tipoIdCliente);
			this.write(numeroIdCliente, numIdCliente);
			this.click(mostrarDatos);
			cont = 0;
			do {
				DXCUtil.wait(1);
				cont++;
			} while (this.element(tablaResultPendSinInfo) == null && cont < 4);

		}

		if (this.element(tablaResultPendSinInfo) != null) {
//			Evidence.save("No hay registros para la consulta", this);
			Evidence.save("No hay registros para la consulta");
			msj[0] = "No se esperaba el mensaje [ " + this.element(tablaResultPendSinInfo).getText() + " ].";
			return msj;
		} else if (this.element(tablaResultPendSinInfo) == null) {
			if (tipoDePrueba.contains("Descargar Archivo con Solicitudes Pendientes")) {
				Reporter.reportEvent(Reporter.MIC_INFO,
						"tipo de prueba: [" + tipoDePrueba + "]" + "Formato de descarga" + "[" + formatoDescarga + "]");
				this.selectListItem(formatoDescargas, formatoDescarga);
				DXCUtil.waitMilisegundos(500);
				Evidence.save("Resultados busqueda");
				this.focus(salarioMensual);
				DXCUtil.wait(1);
				Evidence.save("Resultado busqueda parte 2");
				this.focus(aprobadores);
				DXCUtil.wait(1);
				Evidence.save("Resultados busqueda parte  3");
				this.click(descargar);
				DXCUtil.wait(6);
				msj[0] = "Se realiza descarga de formato con registros pendientes de aprobar, verifique evidencia.";
				return msj;
			}

			if (tipoDePrueba.contains("Cargue de Archivo, Aprobación y Consulta Históricos")) {
				this.click(cargar);
				DXCUtil.wait(2);
				this.selectListItem(formatoDescargaCargue, cargueDelArchivo);
				DXCUtil.waitMilisegundos(600);
				WebElement btnCargueArch = this.element(inpRutaDelArchivo);
				if (btnCargueArch != null) {
					String urlArchivo = SettingsRun.getTestData().getParameter("URL Cargue Archivo");

					this.mouseOver(btnCargueArch);
					this.mouseClick();
					DXCUtil.cargueArchivo(urlArchivo);
					DXCUtil.wait(1);
					this.click(btnCargarArchivo);
					cont = 0;
					do {
						DXCUtil.wait(1);
						cont++;
					} while (this.element(msjErrorCargue) == null && cont < 7);
					cont = 0;
					if (this.element(msjErrorCargue) != null) {
						String msjError = this.getText(msjErrorCargue);
						msj[0] = "	Se visualiza mensaje: " + msjError;
						Evidence.save("Error al cargar archivo");
						return msj;
					}
					// sirve para leer el archivo de cargue de solicitudes pendientes en caso de ser
					// necesario
//					this.controllerDestinosMasivos = new ControllerDestinosMasivos(null); 	
//					String[] datosExcel = controllerDestinosMasivos.BuscarDatosLibranza(urlCargueArchivoExcel);
//					estadoArchivoCargue = new String[datosExcel.length];
//					observacionesArchivoCargue = new String[datosExcel.length];
//					for (int i = 0; i < datosExcel.length; i++) {
//					String[] separarPartes = datosExcel[i].split("\\|"); 
//					estadoArchivoCargue[i] = separarPartes[0];
//					observacionesArchivoCargue[i] = separarPartes[1]; 
//					}

				} else {
					msj[0] = "No fue posible seleccionar archivo de cargue de solicitudes pendientes";
					return msj;
				}

			}

			WebElement tablaResultados = this.findElement(tabResultados);

			List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));

			boolean CheckBoxSeleccionado = false;
			boolean[] numeroCheckSeleccionados = null;
			boolean verificarCoincidencias = false;
			String msjBloqueo = null;
			String xpathRemplazarID = "";
			String almacenarIndice = null;
			String xpathRemplazarNumero = "";
			List<String> indiceCheckBoxList = new ArrayList<String>();
			int contarDatos = 1;
			WebElement checkbox = null;

// For para recorrer todas las filas de la tabla de resultados obtenidos con el objetivo de saber cuantas filas contienen información que coincida con la CC diligenciada en archivo de lanzamiento

			/*
			 * indiceCheckBoxList Se usa para almacenar el numero de la fila donde se
			 * encuentra información que coincide con la CC infoFilaCheckBox Se utiliza para
			 * saber cuantas filas se encuentran con información que coinciden con la CC
			 * numeroCheckSeleccionados Se utiliza para verificar cuantos checkbox fueron
			 * seleccionados
			 */
			for (int i = 0; i < filas.size(); i++) {
				if (solicitudNumero == null) {

					if (filas.get(i).getText().contains(numIdCliente)) {
						infoFilaCheckBox = new WebElement[contarDatos++];
						numeroCheckSeleccionados = new boolean[contarDatos];
						almacenarIndice = String.valueOf((i + 1));
						indiceCheckBoxList.add(almacenarIndice);
						verificarCoincidencias = true;

					}
				} else {

					for (int j = 0; j < solicitudNumero.length; j++) {
						if (filas.get(i).getText().contains(numIdCliente)
								&& filas.get(i).getText().contains(solicitudNumero[j])) {
							infoFilaCheckBox = new WebElement[contarDatos++];
							numeroCheckSeleccionados = new boolean[contarDatos];
							almacenarIndice = String.valueOf((i + 1));
							indiceCheckBoxList.add(almacenarIndice);
							verificarCoincidencias = true;

						}

					}

				}

			}

			if (verificarCoincidencias == false) {
				msj[0] = "No se encontró registros pendientes de aprobar que coincidan con el ID " + numIdCliente;
				Evidence.save("Resultaods busqueda");
				return msj;
			}

			Evidence.save("Resultados busqueda");
			this.focus(salarioMensual);
			DXCUtil.wait(1);
			Evidence.save("Resultado busqueda parte 2");
			this.focus(aprobadores);
			DXCUtil.wait(1);
			Evidence.save("Resultados busqueda parte  3");

			String[] indiceCheckBox = new String[indiceCheckBoxList.size()];
			indiceCheckBox = indiceCheckBoxList.toArray(indiceCheckBox);
			StringBuilder xpathRemplazar = new StringBuilder();

			// si en el archivo de lanzamiento se indicó que van a aprobar más créditos de
			// lo que se presentan en la tabla se va terminar la iteración
			if (nroCreditosAprobaciones.contains("2")) {
				if (indiceCheckBox.length < 2) {
					msj[0] = "No se encontró 2 solicitudes pendientes de aprobar con los datos suministrados, por favor cambie el número de creditos a aprobar en el archivo de lanzamiento. Registros encontrados ["
							+ indiceCheckBox.length + "]";
					return msj;
				}
			}
			if (nroCreditosAprobaciones.contains("3")) {
				if (indiceCheckBox.length < 3) {
					msj[0] = "No se encontró 3 solicitudes pendientes de aprobar con los datos suministrados, por favor cambie el número de creditos a aprobar en el archivo de lanzamiento. Registros encontrados ["
							+ indiceCheckBox.length + "]";
					return msj;
				}
			}

			// // En el siguiente for se arma los xpath para seleccionar el checkbox que
			// coincide con la CC diligenciada en el archivo de lanzamiento en la tabla de
			// resultados en solicitudes pendientes
			for (int b = 0; b < infoFilaCheckBox.length; b++) {

				if (solicitudNumero != null) {
					xpathRemplazarNumero = checkBoxXpath.replace("numIdCliente", solicitudNumero[b]);
					xpathRemplazarID = xpathRemplazarNumero.replace("row", indiceCheckBox[b]);
					checkbox = this.element(By.xpath(xpathRemplazarID));

				} else {
					xpathRemplazarNumero = checkBoxXpath.replace("numIdCliente", numIdCliente);
					xpathRemplazarID = xpathRemplazarNumero.replace("row", indiceCheckBox[b]);
					checkbox = this.element(By.xpath(xpathRemplazarID));
				}
				if (checkbox == null && filas.size() == 1) {
					if (solicitudNumero != null) {
						xpathRemplazarID = seleccionXpathUnicaFila.replace("numIdCliente", solicitudNumero[b]);
					} else {
						xpathRemplazarID = seleccionXpathUnicaFila.replace("numIdCliente", numIdCliente);
					}
					checkbox = this.element(By.xpath(xpathRemplazarID));

				}
				infoFilaCheckBox[b] = checkbox;

				xpathRemplazar.append(xpathRemplazarID);
				if (xpathRemplazar.length() > 0 && b < infoFilaCheckBox.length - 1) {
					xpathRemplazar.append(", ");
				}

			}
			String ubicacionesStr = xpathRemplazar.toString();
			String[] xpathArray = ubicacionesStr.split(",");

			this.focus(this.element(xpathArray[0]));

			boolean salirFor = false;
			int trueCountCheckBox = 0;

			String xpathNumeroSolicitud = null;
			String xpathAbrirCalendarFechaIngreso = null;
			String xpathTipoDeContrato = null;
			String xpathEstado = null;
			String xpathCuotaMontoInferior = null;
			String observ = null;
			String xpathAprobaciones = null;
			String xpathCeldasFila = null;
			String numSolicitud = null;

//			for (WebElement fila : filas) {
//				if (salirFor == true) {
//					break;
//				}

			// unicamente aprobara los que se encuentran en la primera pagina de la tabla de
			// resultados
			String xpathRemplazarFila = "";
			int numFilasComparacion = 1;
			int posicionAlmacenar = 0;
			String[] arrayDatosFila = new String[infoFilaCheckBox.length];

			// En el siguiente for se selecciona los checkbox con los xpath armados
			// previamente y se diligencia la información de los campos habilitados para
			// diligenciar
			// -en la tabla si corresponde, también se realiza la aprobación de los creditos
			// marcados

			for (int j = 0; j < infoFilaCheckBox.length; j++) {

				this.click(By.xpath(xpathArray[j]));

				cont = 0;
				do {
					cont++;
					DXCUtil.wait(1);
				} while (this.element(msjErrorCheckBox) == null && cont <= 2);

				if (this.element(msjErrorCheckBox) != null) {
					msjBloqueo = this.getText(msjErrorCheckBox);
					Reporter.reportEvent(Reporter.MIC_INFO, "Se visualiza mensaje " + msjBloqueo);
					Evidence.save("No permite seleccionar checkbox");
					this.clickButton("Aceptar");
					DXCUtil.wait(1);
					Evidence.save("No permite seleccionar checkbox");
					CheckBoxSeleccionado = false;
					numeroCheckSeleccionados[j] = CheckBoxSeleccionado;
//							this.focus(cuentaMontoInferior);
//							Evidence.save("Registro  pendientes de aprobar");
					continue;
				}
				if (infoFilaCheckBox[j].isSelected()) {
					CheckBoxSeleccionado = true;
					numeroCheckSeleccionados[j] = CheckBoxSeleccionado;

					Reporter.reportEvent(Reporter.MIC_INFO,
							"Se selecciona checkbox con registro encontrado con el ID " + numIdCliente);
					if (CheckBoxSeleccionado) {
						// Se arman xpath que se basan en el xpath del checkbox seleccionado(xpathArray)
						// para diligenciar los campos habilitados en la tabla en la fila correcta
						xpathNumeroSolicitud = "(" + xpathArray[j] + ")/parent::td/parent::tr//td[5]";
						xpathAbrirCalendarFechaIngreso = "(" + xpathArray[j]
								+ ")/parent::td/parent::tr//td[13]/div/button";
						xpathTipoDeContrato = "(" + xpathArray[j] + ")/parent::td/parent::tr//td[14]/select";
						xpathEstado = "(" + xpathArray[j] + "//following::td//select[not(@disabled)])[2]";
						xpathCuotaMontoInferior = "(" + xpathArray[j] + ")/parent::td/parent::tr//td[17]/input";
						observ = "(" + xpathArray[j] + ")/parent::td/parent::tr//td[18]/button";
						xpathAprobaciones = "(" + xpathArray[j] + ")/parent::td/parent::tr//td[19]";
						xpathCeldasFila = "(" + xpathArray[j] + ")/parent::td/parent::tr//td";

						WebElement CuotaMontoInferior = this
								.element(By.xpath("(" + xpathArray[j] + ")/parent::td/parent::tr//td[17]/input"));
						WebElement fechaIngreso = this
								.element(By.xpath("(" + xpathArray[j] + ")/parent::td/parent::tr//td[13]/div/input"));
						WebElement salarioMensual = this
								.element(By.xpath("(" + xpathArray[j] + ")/parent::td/parent::tr//td[15]/input"));

						WebElement numeroSolicitud = this.element(xpathNumeroSolicitud);
						WebElement tipoDeContrato = this.element(xpathTipoDeContrato);
						WebElement estadoLibranza = this.element(xpathEstado);
						numSolicitud = this.getText(numeroSolicitud);
						WebElement CuotaMontoInf = this.element(xpathCuotaMontoInferior);
						By fechaaIngreso = By.xpath(xpathAbrirCalendarFechaIngreso);

						if (tipoDePrueba.contains("Aprobación Solicitudes Pendientes y Consulta Históricos")) {
							SelectCalendarDate(fechaIngresoEmp, fechaaIngreso, monthCalendar, dayElementXpathSolPend,
									yearCalendar);
							this.selectListItem(tipoDeContrato, tipoContrato);
							this.selectListItem(estadoLibranza, estadoSolicitud);
							this.write(salarioMensual, salarioMensualEmple);
							if (estadoSolicitud.contains("Inferior")) {
								this.write(CuotaMontoInf, cuotaMontoInfer);
								Evidence.save("Cuota Monto Inferior Diligenciado Solicitud: " + numSolicitud);
							}
							this.click(By.xpath(observ));

							cont = 0;
							do {
								DXCUtil.wait(1);
								cont++;
							} while (this.element(inputObservacion) == null && cont == 5);
							this.write(inputObservacion, observaciones);
							this.click(btnGuardar);
							DXCUtil.wait(1);
						}

						List<WebElement> celdasFila = this.findElements(By.xpath(xpathCeldasFila));
						List<String> valoresTabla = new ArrayList<String>();
						List<String> datosAComparar = new ArrayList<String>();

						String datosObtenidosFila;
						// for para extraer la información diligenciada en la fila actual y luego
						// almacenar esta información en un array para compararla contra la información
						// que se presenta en el modulo de consulta históricos
						for (int s = 2; s < celdasFila.size(); s++) {
							List<WebElement> selects = celdasFila.get(s).findElements(By.tagName("select"));
							if (!selects.isEmpty()) {
								WebElement selectElement = selects.get(0); // Suponiendo un solo select por
																			// celda

								Select select = new Select(selectElement);

								String selectedText = select.getFirstSelectedOption().getText();

								valoresTabla.add(selectedText);

							} else {
								String cellText = "";
								cellText = celdasFila.get(s).getText();

								if (s == 12 && cellText.isEmpty()) {

									String obtenerFechaIngreso = this.getText(fechaIngreso);
									valoresTabla.add(obtenerFechaIngreso);

								}

								if (s == 14 && cellText.isEmpty()) {

									String obtenerSalarioMensual = this.getText(salarioMensual);
									valoresTabla.add(obtenerSalarioMensual);

								}
								if (s == 16 && cellText.isEmpty()) {

									String obtenerCuotaMontoInf = this.getText(CuotaMontoInferior);
									valoresTabla.add(obtenerCuotaMontoInf);

								}

								// Obtener el texto de la celda que no contiene un select

								valoresTabla.add(cellText);
								if (s == 17) {
									if (tipoDePrueba
											.contains("Aprobación Solicitudes Pendientes y Consulta Históricos")) {
										valoresTabla.set(18, observaciones);

									} else {
										if (tipoDePrueba
												.contains("Cargue de Archivo, Aprobación y Consulta Históricos")) {
											this.click(By.xpath(observ));

											cont = 0;
											do {
												DXCUtil.wait(1);
												cont++;
											} while (this.element(inputObservacion) == null && cont == 5);
											String observacionesCargue = this.getText(inputObservacion);
											if (observacionesCargue == null || observacionesCargue == "") {
												observacionesCargue = " ";
											}
											this.click(btnGuardar);
											DXCUtil.wait(1);
											valoresTabla.set(18, observacionesCargue);
										}
									}
								}

							}
						}
						String fecha = valoresTabla.get(0);
						String codigoConvenio = valoresTabla.get(1);
						String solicitud = valoresTabla.get(2);
						String numID = valoresTabla.get(3);
						String nombresApellidos = valoresTabla.get(4);
						String producto = valoresTabla.get(5);
						String entidadCompraCartera = valoresTabla.get(6);
						String valorCredito = valoresTabla.get(7);
						String numeroCuotas = valoresTabla.get(8);
						String valorCuota = valoresTabla.get(9);
						String fechaIngresoEmpl = valoresTabla.get(10);
						String tipoContratoEmpl = valoresTabla.get(12);
						String salarioMensualEmpl = valoresTabla.get(13);
						String estadoEmple = valoresTabla.get(15);
						String MontInferEmp = valoresTabla.get(16);
						String observEmp = valoresTabla.get(18);
						String numeroFirmas = valoresTabla.get(19);
						Reporter.reportEvent(Reporter.MIC_INFO, ("Información solicitud [" + solicitud
								+ "] antes de aprobar, se diligenció información ingresada en archivo de lanzamiento [ "
								+ "Fecha " + "|" + fecha + "|" + " Convenio " + "|" + codigoConvenio + "|" + " numID "
								+ "|" + numID + "|" + " Nombres y apelidos " + "|" + nombresApellidos + "|"
								+ " Producto " + "|" + producto + "|" + "Entidad Compra y Cartera " + "|"
								+ entidadCompraCartera + "|" + " Valor credito " + "|" + valorCredito + "|"
								+ " Numero cuotas " + "|" + numeroCuotas + "|" + " Valor cuota " + "|" + valorCuota
								+ "|" + " FechaIngreso Empleado " + "|" + fechaIngresoEmpl + "|"
								+ " Tipo contrato empleado " + "|" + tipoContratoEmpl + "|"
								+ " Salario mensual Empleado " + "|" + salarioMensualEmpl + "|" + " Estado " + "|"
								+ estadoEmple + "|" + " Monto Inferior Empleado " + "|" + MontInferEmp + "|"
								+ " observEmp " + "|" + observEmp + "|" + " Aprobadores " + "|" + numeroFirmas + "|"
								+ " ]"));
						if (!numeroFirmas.contains("0/1")) {
							if (numeroAprobaciones.contains("1")) {
								msj[0] = "Por favor verifique parametrización de número de firmas, en el archivo de lanzamiento en la columna Números de Aprobaciones se seleccionó 1 Aprobación pero en front pyme dice que para aprobar se requieren "
										+ numeroFirmas + " para aprobar las solicitudes pendientes";
								return msj;
							}
						}
						datosAComparar.add(codigoConvenio);
						datosAComparar.add(solicitud);
						datosAComparar.add(numID);
						datosAComparar.add(entidadCompraCartera);
						datosAComparar.add(valorCredito);
						datosAComparar.add(numeroCuotas);
						datosAComparar.add(valorCuota);
						datosAComparar.add(fechaIngresoEmpl);
						datosAComparar.add(tipoContratoEmpl);
						datosAComparar.add(salarioMensualEmpl);
						datosAComparar.add(estadoEmple);
						datosAComparar.add(MontInferEmp);
						datosAComparar.add(observEmp);
						StringBuilder datosStringFila = new StringBuilder();
						for (int a = 0; a < datosAComparar.size(); a++) {
							datosStringFila.append(datosAComparar.get(a));
							if (a < datosAComparar.size() - 1) {
								datosStringFila.append(", ");
							}
						}
						datosObtenidosFila = datosStringFila.toString();
						arrayDatosFila[posicionAlmacenar++] = datosObtenidosFila;

					}
				}
				// se verifica que se seleccione solo los checkbox que dice en el archivo de
				// lanzamiento para que se apruebe 1 o multiples créditos de libranza
				if (numeroCheckSeleccionados[j]) {
					trueCountCheckBox++;

					if (trueCountCheckBox == 1 && nroCreditosAprobaciones.equalsIgnoreCase("1")) {
						salirFor = true;
						break;
					} else if (trueCountCheckBox == 2 && nroCreditosAprobaciones.equalsIgnoreCase("2")) {
						salirFor = true;
						break;
					} else if (trueCountCheckBox == 3 && nroCreditosAprobaciones.equalsIgnoreCase("3")) {
						salirFor = true;
						break;
					} else if (j == infoFilaCheckBox.length - 1 && nroCreditosAprobaciones.equalsIgnoreCase("todos")) {
						salirFor = true;
						break;
					}

				}

			}
//						}

//					}
//				}

//					}  

//			}
			if (trueCountCheckBox == 0) {
				msj[0] = "No permitio seleccionar ningún checkbox con el ID " + numIdCliente;
				return msj;
			}

			// se crea una lista para almacenar los valores no nulos de las filas que se
			// aprobo la solicitud de manera exitosa
			List<String> listaTemporal = new ArrayList<String>();
			// recorre la lista y añade los valores no nulos a la lista
			for (String validacion : arrayDatosFila) {
				if (validacion != null) {
					listaTemporal.add(validacion);
				}
			}
			// Se crea un nuevo array con la lista con información de las filas que se
			// aprobó
			String[] arrayDatosFilas = listaTemporal.toArray(new String[0]);

			// se utiliza focus en el primer checkbox seleccionado
			this.focus(this.element(xpathArray[0]));
			DXCUtil.wait(1);
			Evidence.save("Info diligenciada antes de aprobar");
			this.focus(salarioMensual);
			DXCUtil.wait(1);
			Evidence.save("Info diligenciada antes de aprobar parte 2");
			this.focus(aprobadores);
			DXCUtil.wait(1);
			Evidence.save("Info diligenciada antes de aprobar parte 3");

			this.clickButton("Aprobar");
			DXCUtil.wait(3);
			Evidence.save("Msj Confirmación 1");
			this.click(aceptar);
			DXCUtil.wait(2);
			Evidence.save("Msj Confirmación 2");
			this.click(aceptarConfirm);
			DXCUtil.wait(2);
			Evidence.save("Msj 3");
			String msjConfirmacion = this.getText(validarMsj);
			this.click(aceptarConfirm);
			DXCUtil.wait(2);
			this.focus(tipoIDCliente);
			Evidence.save("Registros solicitudes después de aprobación");
			DXCUtil.wait(2);

			if (msjConfirmacion.contains("Sus solicitudes han sido procesadas")) {
				return arrayDatosFilas;
			} else if (msjConfirmacion.contains("Solicite al siguiente usuario autorizado aprobar la solicitud")) {

				return arrayDatosFilas;

			} else if (msjConfirmacion.contains("¿Está seguro que desea actualizar la información?")) {

				return arrayDatosFilas;

			} else {

				msj[0] = "Se visualiza mensaje " + msjConfirmacion;

			}
		}

		return msj;

	}

	public String[] consultaHistoricos(String[] solicitudNumero, String[] DatosFilasComparacion) throws Exception {

		// variables para obtener la información de la prueba a realizar en el excel
		String rangoFechaDesde = SettingsRun.getTestData().getParameter("Rango Fecha Desde");
		String rangoFechaHasta = SettingsRun.getTestData().getParameter("Rango Fecha Hasta");
		String tipoIdCliente = SettingsRun.getTestData().getParameter("Tipo ID Cliente");
		String numIdCliente = SettingsRun.getTestData().getParameter("Número ID Cliente");
		String estadoSolicitud = SettingsRun.getTestData().getParameter("Estado de solicitud");
		String convenio = SettingsRun.getTestData().getParameter("Convenio");
		String filtroBusqConsHistoricos = SettingsRun.getTestData().getParameter("Filtro de busqueda Consulta Historicos");
		// variable para encontrar el elemento dia en calendario por medio de un xPath

		String dayElementXpathConsuHist = "(//*[@class='ngb-dp-month d-block']//div/div/div[(text()= 'dia'  and (not(contains(@class,'text-muted outside'))))])[1]";

		String infoSolicitud = null;
		String[] msg = new String[1];

//		this.pageOrigen = new PageOrigen(this);
//
//		do {
//			DXCUtil.wait(1);
//
//		} while (this.element(Menu) == null && cont <= 24);
//
//		msg[0] = this.pageOrigen.irAOpcion("", "Gestión Recursos Humanos", "Libranza", "Autorizaciones Libranza");
//
//		if (msg[0] != null) {
//			return msg;
//		}
//
//		WebElement iframe = null;
//		do {
//			iframe = this.element(frameLibranza);
//			DXCUtil.wait(1);
//		} while (iframe == null);
//		this.changeToFirstFrame();
//
//		WebElement selconvenio = null;
//		do {
//			DXCUtil.wait(1);
//			selconvenio = this.element(seleccionConvenio.replace("convenio", convenio));
//			cont++;
//		} while (selconvenio == null && cont <= 24);
//		cont = 0;
//		if (selconvenio != null) {
//			this.click(selconvenio);
//		}
//		do {
//			DXCUtil.wait(1);
//		} while (this.element(moduloConsultaHist) == null);

		this.click(moduloConsultaHist);

		cont = 0;
		do {
			DXCUtil.wait(1);
			cont++;
		} while (this.element(convenios) == null && cont < 4);
		if (this.element(convenios) == null) {
			this.click(moduloConsultaHist);
			DXCUtil.wait(2);
		}
		Reporter.write(" ");
		Reporter.write("==============================[Consulta Históricos]==============================");
		Reporter.write(" ");

		// se realiza el filtro de busqueda de acuerdo a lo diligenciado en archivo de
		// lanzamiento

		if (filtroBusqConsHistoricos.equals("Estado de solicitud  y Convenio")) {
//			this.selectListItem(estado, item)
			Reporter.reportEvent(Reporter.MIC_INFO, "Parámetro de búsqueda: " + " [" + filtroBusqConsHistoricos + "]"
					+ " Estado" + " [" + estadoSolicitud + "]" + " Convenio " + " [" + convenio + "]");

			this.selectListItem(estadoSol, estadoSolicitud);
			DXCUtil.waitMilisegundos(600);
			this.seleOptionIgual(convenios, convenio);
			DXCUtil.waitMilisegundos(600);
			Evidence.save("Filtro");
			this.click(mostrarDatos);

		} else if (filtroBusqConsHistoricos.equals("Rango de Fechas y Convenio")) {

			Reporter.reportEvent(Reporter.MIC_INFO,
					"Parámetro de búsqueda: " + "[" + filtroBusqConsHistoricos + "]" + " Rango fecha desde " + " ["
							+ rangoFechaDesde + "]" + "Rango fecha hasta " + " [" + rangoFechaHasta + "]" + " Convenio"
							+ "[" + convenio + "]");
			SelectCalendarDate(rangoFechaDesde, abrirCalendarFechaDesde, monthCalendar, dayElementXpathConsuHist,
					yearCalendar);
			DXCUtil.wait(1);
			SelectCalendarDate(rangoFechaHasta, abrirCalendarFechaHasta, monthCalendar, dayElementXpathConsuHist,
					yearCalendar);
			DXCUtil.wait(1);
			this.seleOptionIgual(convenios, convenio);
			DXCUtil.waitMilisegundos(600);
			this.click(mostrarDatos);

		} else if (filtroBusqConsHistoricos.equals("Tipo de documento, Número ID cliente y Convenio")) {
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Parámetro de búsqueda: " + "[" + filtroBusqConsHistoricos + "]" + " Tipo de documento " + " ["
							+ tipoIdCliente + "]" + "Número ID Cliente " + " [" + numIdCliente + "]" + " Convenio"
							+ " [" + convenio + "]");
			this.selectListItem(tipoIDCliente, tipoIdCliente);
			this.write(numclienteEmp, numIdCliente);
			this.seleOptionIgual(convenios, convenio);
			DXCUtil.waitMilisegundos(600);
			Evidence.save("Filtro");
			this.click(mostrarDatos);

		}
		cont = 0;
		do {
			DXCUtil.wait(1);
			cont++;
			if (this.element(msjError) != null) {
				String msjErr = this.getText(msjError);
//				Evidence.save("Mensaje error", this);
				Evidence.save("Mensaje error");
				msg[0] = "No se esperaba el mensaje: " + msjErr;
				return msg;

			}
		} while (this.element(tablaResultConsulHisSinInfo) == null && cont < 4);

		if (this.element(tablaResultConsulHisSinInfo) != null) {
//			Evidence.save("No hay registros para la consulta", this);
			Evidence.save("No hay registros para la consulta");
			msg[0] = "No se esperaba el mensaje [ " + this.element(tablaResultConsulHisSinInfo).getText() + " ].";
			return msg;
		} else if (this.element(tablaResultConsulHisSinInfo) == null) {

			WebElement buscarFilaConSolic = null;
			String[] resultadosComparación = new String[solicitudNumero.length];
			for (int i = 0; i < solicitudNumero.length; i++) {
				String consultaXpath = consultaHistFilaXpath.replace("numeroSolicitud", solicitudNumero[i]);
				cont = 0;
				do {
					DXCUtil.wait(2);
					buscarFilaConSolic = this.element(By.xpath(consultaXpath));
					if (buscarFilaConSolic == null && this.element(btnSiguiente) != null) {
						this.click(btnSiguiente);
					}
					if (this.element(btnSiguiente) == null) {
						cont++;
					}

				} while (buscarFilaConSolic == null && cont < 2);
				cont = 0;
				if (this.element(btnSiguiente) == null && buscarFilaConSolic == null) {
					if (this.element(btnPrimero) != null) {
						this.click(btnPrimero);
					}
				}
				if (buscarFilaConSolic != null) {

					// infoSolicitud extrae la información retornada en consulta historicos y luego
					// se compara con la informacón del array DatosFilasComparacion que tiene la
					// información diligenciada en solicitudes pendientes
					infoSolicitud = this.getText(buscarFilaConSolic);
					Reporter.reportEvent(Reporter.MIC_INFO, "Información Encontrada con solicitud " + "["
							+ solicitudNumero[i] + "]" + " = " + infoSolicitud);
					String datosFila = DatosFilasComparacion[i];
					String[] datosFilaSeparados = datosFila.split(", ");
					String codigoConvenioHist = datosFilaSeparados[0];
					String solicitudHist = datosFilaSeparados[1];
					String numIDHist = datosFilaSeparados[2];
					String entidadCompraCarteraHist = datosFilaSeparados[3];
					String valorCreditoHist = datosFilaSeparados[4];
					String numeroCuotasHist = datosFilaSeparados[5];
					String valorCuotaHist = datosFilaSeparados[6];
					String fechaIngresoEmplHist = datosFilaSeparados[7];
					String fechaFormateada = formatearFecha(fechaIngresoEmplHist);
					fechaIngresoEmplHist = fechaFormateada;
					String tipoContratoEmpl = datosFilaSeparados[8];
					String salarioMensualEmplHist = datosFilaSeparados[9];
					String estadoEmpleHist = datosFilaSeparados[10];
					String MontInferEmpHist = datosFilaSeparados[11];
					String observEmpHist = datosFilaSeparados[12];
					String ResultadoComparacion = "";

					if (infoSolicitud.contains(codigoConvenioHist)) {
						ResultadoComparacion = "Código convenio: OK, ";
					} else {
						ResultadoComparacion = "Código convenio: Incorrecto, ";
					}

					if (infoSolicitud.contains(solicitudHist)) {
						ResultadoComparacion = ResultadoComparacion + "Solicitud: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Solicitud: Incorrecto, ";
					}

					if (infoSolicitud.contains(numIDHist)) {
						ResultadoComparacion = ResultadoComparacion + "Número ID: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Número ID: Incorrecto, ";
					}

					if (infoSolicitud.contains(entidadCompraCarteraHist)) {
						ResultadoComparacion = ResultadoComparacion + "Entidad Compra y Cartera: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Entidad Compra y Cartera: Incorrecto, ";
					}

					if (infoSolicitud.contains(valorCreditoHist)) {
						ResultadoComparacion = ResultadoComparacion + "Valor Crédito: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Valor Crédito: Incorrecto, ";
					}

					if (infoSolicitud.contains(numeroCuotasHist)) {
						ResultadoComparacion = ResultadoComparacion + "Número de Cuotas: OK, ";

					} else {
						ResultadoComparacion = ResultadoComparacion + "Número de Cuotas: Incorrecto, ";
					}

					if (infoSolicitud.contains(valorCuotaHist)) {
						ResultadoComparacion = ResultadoComparacion + "Valor Cuota: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Valor Cuota: Incorrecto, ";
					}

					if (infoSolicitud.contains(fechaIngresoEmplHist)) {
						ResultadoComparacion = ResultadoComparacion + "Fecha Ingreso Empleado: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Fecha Ingreso Empleado: Incorrecto, ";
					}

					if (infoSolicitud.contains(tipoContratoEmpl)) {
						ResultadoComparacion = ResultadoComparacion + "Tipo de Contrato Empleado: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Tipo de Contrato Empleado: Incorrecto, ";
					}

					if (infoSolicitud.contains(salarioMensualEmplHist)) {
						ResultadoComparacion = ResultadoComparacion + "Salario Mensual: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Salario Mensual: Incorrecto, ";
					}

					if (infoSolicitud.contains(estadoEmpleHist)) {
						ResultadoComparacion = ResultadoComparacion + "Estado: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Estado: Incorrecto, ";
					}

					if (infoSolicitud.contains(MontInferEmpHist)) {
						ResultadoComparacion = ResultadoComparacion + "Cuota Monto Inferior: OK, ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Cuota Monto Inferior: Incorrecto, ";
					}

					if (observEmpHist.contains(" ")) {
						if (infoSolicitud.contains("") || infoSolicitud.contains(" ") || infoSolicitud == null) {
							ResultadoComparacion = ResultadoComparacion + "Observaciones: OK ";
						}
					} else if (infoSolicitud.contains(observEmpHist)) {

						ResultadoComparacion = ResultadoComparacion + "Observaciones: OK ";
					} else {
						ResultadoComparacion = ResultadoComparacion + "Observaciones Incorrecto";
					}

					resultadosComparación[i] = ResultadoComparacion;
				} else {

					resultadosComparación[i] = "No se encontró registros con el número de solicitud "
							+ solicitudNumero[i];

				}

			}
			Evidence.save("Resultados consulta historicos");
			this.focus(tipoContratoHis);
			DXCUtil.wait(1);
			Evidence.save("Resultados consulta historicos parte 2");
			DXCUtil.wait(1);
			this.focus(respuestaBancoHis);
			DXCUtil.wait(1);
			Evidence.save("Resultados consulta historicos parte 3");

			return resultadosComparación;

		}

		return msg;

	}

	/**
	 * Metodo para seleccionar una fecha del calendario
	 * 
	 * @param fecha            Se toma del archivo de excel en formato DD/MM/AAAA
	 * @param abrirCalendario  Localizador para seleccionar calendario
	 * @param mes              Localizador para seleccionar el mes
	 * @param año              Localizador para seleccionar el año
	 * @param elementoDiaXpath localizador xPath que debe tener el texto 'dia' para
	 *                         ser remplazado en el metodo por el dia ingresado en
	 *                         el campo fecha
	 */
	public void SelectCalendarDate(String fecha, By abrirCalendario, By mes, String elementoDiaXpath, By año) {
		// Divide la fecha en día, mes y año
		String[] dateParts = fecha.split("/");
		String day = dateParts[0];
		String month = dateParts[1];
		String year = dateParts[2];

		// Convertir el día a un número entero para eliminar los ceros a la izquierda

		int dayInt = Integer.parseInt(day);
		String dayFormatted = String.valueOf(dayInt);

		String dayElementRemplazado = elementoDiaXpath.replace("dia", dayFormatted);
		// Convertir el mes a un número entero para eliminar los ceros a la izquierda
		int monthInt = Integer.parseInt(month);
		String monthFormatted = String.valueOf(monthInt);

		// Mapear el mes numérico a su representación en texto
		String[] months = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
		String monthText = months[Integer.parseInt(monthFormatted) - 1];

		// Abre el calendario
		WebElement calendarButton = this.findElement(abrirCalendario);
		calendarButton.click();

		// Espera a que el calendario sea visible
		cont = 0;
		do {
			DXCUtil.wait(1);
			cont++;
		} while (mes == null && cont < 5);
		if (mes == null) {
			DXCUtil.wait(2);
			calendarButton.click();
		}

		// Selecciona el mes
		Select monthSelect = new Select(this.findElement(mes));
		monthSelect.selectByVisibleText(monthText);

		// Selecciona el año
		Select yearSelect = new Select(this.findElement(año));
		yearSelect.selectByVisibleText(year);

		// Selecciona el día
		WebElement dayElementoFinal = null;
		dayElementoFinal = this.element(dayElementRemplazado);
		dayElementoFinal.click();
	}

	/**
	 * Metodo para cambiar el formato de una fecha AAAA/M/D a formato AAAA/MM/DD.
	 * Ejemplo: 2020/1/1 convierte la fecha a 2020/01/01
	 * 
	 * @param fecha
	 * @return
	 */
	public static String formatearFecha(String fecha) {

		// Patrón para el formato correcto

		String patronCorrecto = "\\d{4}/\\d{2}/\\d{2}";

		// Verificar si la fecha ya está en el formato correcto

		if (fecha.matches(patronCorrecto)) {

			return fecha;

		}

		try {

			// Definir el patrón de entrada

			DateTimeFormatter entradaFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");

			// Analizar la fecha de entrada

			LocalDate date = LocalDate.parse(fecha, entradaFormatter);

			// Definir el patrón de salida

			DateTimeFormatter salidaFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			// Formatear la fecha de salida

			return date.format(salidaFormatter);

		} catch (DateTimeParseException e) {

			e.printStackTrace();

			return null;

		}

	}

}
