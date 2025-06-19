package launchTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.middlePymes.*;
import dav.ActualizacionDeDatos.PageActualizacionDeDatos;
import dav.CobrosMiddle.ControllerMiddleCobros;
import dav.CobrosMiddle.PageAdminCombosCobros;
import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.TransaccionesProgramadas.PageTransaccionesProgramadas;
import dav.c360.PageInicioC360;
import dav.c360.PageLogin;
import dav.divisas.*;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloCrearTx.ControllerDestinosMasivos;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class LaunchTestPyme extends BaseTestNG {

// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageDivisas pageDivisas = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageAdminParametros pageAdminParametros = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	ControllerCrearTx controller = null;
	ControllerDestinosMasivos controllerDestinosMasivos = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;
	PageAprobacionInter pageAprobInter = null;
	PageConsultatxInternacional pageConsultatxInternacional = null;
	PageDocumentos_Y_Formularios pageDocumentos_Y_Formularios = null;
	PageTransaccionesProgramadas pageTransaccionesProgramadas = null;
	ControllerMiddleCobros controllerMiddleCobros = null;
	PageAdminCombosCobros pageAdminCombosCobros = null;
	PageActualizacionDeDatos pageActualizacionDeDatos = null;
	PageLogin pageLoginC360 = null;
	PageInicioC360 pageInicioC360 = null;
// =======================================================================================================================	

// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR
	final String TP_LOGIN = "Login";
	final String TP_EN_LINEA = "Tx En Línea";
	final String TP_PEND_APR = "Tx Pend Aprobación";
	final String CN_APRO_PEND = "1";
	final String DE_El_DETALLE = "SI";
	String transaccion = MotorRiesgo.TX_EMP_LOGIN_SUCC; // VALOR POR DEFECTO

// -----------------------------------------------------------------------------------------------------------------------

	String nombreAmbiente = null;
	String contratacion = null;
	String cobros = null;
	String stratus = null;
	public static String realizarMR, ipPublica = null;
	Date fechaHoraLogMR;
	String tipoPrueba, desde_el_Detalle, servicio, riesgoBc, riesgoEfm, riesgo, userAgent, navegador, empresa,
			tipoIDEmpresa, numeroIDEmpresa, tipoIdentificacion, Idusuario, numAprobaciones, informe;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String Motorna = "N/A";
// =======================================================================================================================	
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre;

	// Variables para guardar los últimos valores
	private String lastNumAprobaciones = "";
	private String lastTipoAbono = "";
	private String lastCtaInscrita = "";
	private String lastIdusuario = "";
	private String lastempresa = "";

// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");

	By ip = By.xpath("/html/body/pre");

// =======================================================================================================================
	List<HashMap<String, String>> resultsMiddle = new ArrayList<>();
	HashMap<String, Double> segmentosStratus = new HashMap<String, Double>();

	double[] validacionDescuento = null;
	String[] estadotxFinal = null;
	String validarCliente = "";
// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO

//		SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // OBLIGATORIO - NOMBRE DE LA CARPETA DE EVIDENCIAS
//		Evidence.siTomarla(Evidence.FORMATO_EXCEL); // TOMAR EVIDENCIAS POR ROW Y DEJA LO ÚLTIMO EN EL FORMATO INDICADO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
//		Evidence.siTomarla(Evidence.FORMATO_WORD); // TOMAR EVIDENCIAS POR ROW Y DEJA LO ÚLTIMO EN EL FORMATO INDICADO
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTÁ EN LA FILA 4 : USARLO SI EL HEADER NO ESTÁ
										// EN 1

// -----------------------------------------------------------------------------------------------------------------------

	}

//	public void cargarTitleYDatosEvidence() throws Exception { // PARA TÍTULOS Y DATA DE EVIDENCIAS
//		super.cargarTitleYDatosEvidence();
//		// CUANDO SE TRATA DE EXCEL O WORD PARA LA CARGA DE EVIDENCIAS, ESTOS SON LOS DATOS CLAVE A MOSTRAR
//		Evidence.testData = "Sistema Operativo" + " - " + "Dato1" + " - " + "Dato2";
//	}
// =======================================================================================================================	

// ====================================LAUNCHCLOSE===================================================================================

	public void launchClose() { // CIERRE DEL LANZAMIENTO
		SettingsRun.CREATE_FINAL_EVIDENCES_WHEN_NOPDF = false;
// -----------------------------------------------------------------------------------------------------------------------
		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
		if (DatosDavivienda.RISKMOTOR != null && SettingsRun.esIteracionFinal())
			DatosDavivienda.RISKMOTOR.cerrarMotorRiesgo();

// -----------------------------------------------------------------------------------------------------------------------

		if (this.pageLogin != null)
			if (this.pageLogin.ThereareOpenWindows())
				this.pageLogin.closeAllBrowsers();
// -----------------------------------------------------------------------------------------------------------------------
		if (DatosDavivienda.STRATUS != null)
			// Se comenta la siguiente linea para que no interactue con stratus
			DatosDavivienda.STRATUS.closeStratus();
//	 if (this.controller != null)
//	 this.controller.destroy();

	}
// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES

	public void doingConfigurations() throws Exception {
		// -----------------------------------------------------------------------------------------------------------------------
		Reporter.writeTitle("\n*** PRUEBAS PORTAL PYME ***");

		// 1.1: Abrir WinAppDriver si no se encuentra abierto - isaac
		DXCUtil.startWinAppDriver();
		// PARÁMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTÁ
		// HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selección"
//						, "Tx Finalizada", "Tx FAIL"
		};
		// HACE LA CONFIGURACIÓN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// ------------------
		// -----------------------------------------------------------------------------------------------------
		// SELECIONA SI CONFIA EN LA CONTRATACIÓN

		nombreAmbiente = SettingsRun.getGlobalData("AMBIENTE_PYME");

		switch (nombreAmbiente) {
		case "1":
		case "PROYECTOS":
			nombreAmbiente = "PROYECTOS";
			break;
		case "2":
		case "CONTENCION":
			nombreAmbiente = "CONTENCION";
			break;
		case "3":
		case "OBSOLESCENCIA":
			nombreAmbiente = "OBSOLESCENCIA";
			break;
		case "4":
		case "ONPREMISE":
			nombreAmbiente = "ONPREMISE";
			break;
		case "5":
		case "POST_NUBE":
			nombreAmbiente = "POST_NUBE";
			break;
		case "6":
		case "CONTENCION_NUBE":
			nombreAmbiente = "CONTENCION_NUBE";
			break;
		case "7":
		case "PROYECTOS_NUBE":
			nombreAmbiente = "PROYECTOS_NUBE";
			break;
		case "8":
		case "MEJORAS":
			nombreAmbiente = "MEJORAS";
			break;
		default:
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no válida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: Portal - " + nombreAmbiente);
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: Portal - " + nombreAmbiente);
		}

		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

		contratacion = SettingsRun.getGlobalData("CONTRATACION");
		cobros = SettingsRun.getGlobalData("VALIDACION.COBROS");
		stratus = SettingsRun.getGlobalData("VALIDAR.STRATUS");
		realizarMR = SettingsRun.getGlobalData("MOTOR.motorDeRiesgo");

		if (realizarMR.equals("SI")) {
			this.riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
			this.riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();

			if (this.riesgoBc.isEmpty() || this.riesgoBc == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Falta Ingresar Datos de MR, Campos Nivel de Riesgo BC ");
				Reporter.write("Falta Ingresar Datos de MR, Campos Nivel de Riesgo BC ");
				if (this.pageOrigen != null)
					this.pageOrigen.terminarIteracion();

				this.launchClose();

			} else if (this.riesgoEfm.isEmpty() || this.riesgoEfm == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Falta Ingresar Datos de MR, Campos Nivel de Riesgo SAS EFM ");
				Reporter.write("Falta Ingresar Datos de MR, Campos Nivel de Riesgo SAS EFM ");
				if (this.pageOrigen != null)
					this.pageOrigen.terminarIteracion();

				this.launchClose();
			}
			// -----------------------------------------------------------------------------------------------------------------------
			// ADICIÓN DE NUEVOS PARÁMETROS REQUERIDOS MR
//				SettingsRun.getGlobalData().addParametersNotExist("Fecha Transacción", "Hora Transacción","Número Aprobación");
			// -----------------------------------------------------------------------------------------------------------------------
			// CONFIGURACIóN DEL ARCHIVO DEL SET DE MOTOR DE RIESGO
			DatosDavivienda.IS_RISKMOTOR = true; // DEJA MARCA QUE INDICA QUE SE TRANSARÁ PARA MOTOR DE RIESGO

			String nbArchivo = MotorRiesgo.preguntarPorArchivoMR();

			this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();

			if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
					&& !this.servicio.equals("Tx Internacionales Enviar al exterior")) {

				if (nbArchivo == null)

					/*
					 * CREA EL ARCHIVO DE MOTOR DE RIEGOS, APARTIR DE LA PANTILLA QUE SE EN CUENTRA
					 * EN EL PROTECTO Davivienda EN "resources" EN LA CARPETA "Template" ARCHIVO
					 * "TPT_MREmpresarial.xlsx"
					 */
					DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT);
// -----------------------------------------------------------------------------------------------------------------------
				else // HAY UN ARCHIVO DE MOTOR DE RIESGO
					/*
					 * ESTE ARCHIVO DEBE DE TENER LOS CAMPOS MÍNIMOS QUE SE REQUIERE PARA EL ARCHIVO
					 * DE MR ESTOS CAMPOS SE ENCUENTRAN PARAMETRIZADOS EN LA CLASE DE MotorRiesgo
					 * METODO "getDataArchivoMR" "case DatosDavivienda.CANAL_PYME_FRONT"
					 */
					DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT, nbArchivo);

			} else if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")
					|| this.servicio.equals("Tx Internacionales Enviar al exterior")) {
				if (nbArchivo == null)
					/*
					 * CREA EL ARCHIVO DE MOTOR DE RIEGOS, APARTIR DE LA PANTILLA QUE SE EN CUENTRA
					 * EN EL PROTECTO Davivienda EN "resources" EN LA CARPETA "Template" ARCHIVO
					 * "TPT_MREmpresarialansd_Divisas_TrasferenciasInternacional.xlsx"
					 */
					DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT_DIVISAS);
// -----------------------------------------------------------------------------------------------------------------------
				else // HAY UN ARCHIVO DE MOTOR DE RIESGO
					/*
					 * ESTE ARCHIVO DEBE DE TENER LOS CAMPOS MÍNIMOS QUE SE REQUIERE PARA EL ARCHIVO
					 * DE MR ESTOS CAMPOS SE ENCUENTRAN PARAMETRIZADOS EN LA CLASE DE MotorRiesgo
					 * METODO "getDataArchivoMR" "case DatosDavivienda.CANAL_PYME_FRONT"
					 */
					DatosDavivienda.RISKMOTOR = new MotorRiesgo(DatosDavivienda.CANAL_PYME_FRONT_DIVISAS, nbArchivo);
			} else if (this.servicio.equals("Tx Internacionales Enviar al exterior Pendiente Aprobación")
					|| this.servicio.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado")) {

				Reporter.reportEvent(Reporter.MIC_FAIL,
						"En estos escenarios No tiene contemplado MR: " + this.servicio);
				this.pageOrigen.terminarIteracion();
			}

		}

	}

// =======================================================================================================================

// ===========================================DOINGTEST===========================================================================

	// MÉTODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
		this.runAutomationBetweenHours(SettingsRun.getGlobalData("TiemPermitido"),
				SettingsRun.getGlobalData("HoraDesVentana"), SettingsRun.getGlobalData("HoraTerVentana"));

		if (realizarMR.equals("SI")) {
			if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
					&& !this.servicio.equals("Tx Internacionales Enviar al exterior")) {
				this.riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
				this.riesgoEfm = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();

				if (SettingsRun.getGlobalData("MOTOR.tipoMotor").contains("EFM")) {
					this.riesgo = this.riesgoEfm;

				} else if (SettingsRun.getGlobalData("MOTOR.tipoMotor").contains("BC")) {
					this.riesgo = this.riesgoBc;
				}

			} else if (this.servicio.equals("Tx Internacionales Recibir desde el exterior")
					|| this.servicio.equals("Tx Internacionales Enviar al exterior")) {
				this.riesgoBc = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
				this.riesgo = this.riesgoBc;
			}
		}
// -----------------------------------------------------------------------------------------------------------------------
		/*
		 * Datos Fijos Middle Login, estos datos se encuentran el archivo
		 * data.properties
		 */
		// numCli tipoDoc numDoc clave tipoTok datoTok
		DatosEmpresarial.loadLoginDataFija("0", SettingsRun.getGlobalData("MIDDLE.tipoDoc"),
				SettingsRun.getGlobalData("MIDDLE.numeroDeId"), SettingsRun.getGlobalData("MIDDLE.clavePersonal"),
				SettingsRun.getGlobalData("MIDDLE.tipoToken"), SettingsRun.getGlobalData("MIDDLE.numeroToken"));

// -----------------------------------------------------------------------------------------------------------------------

		// Organiza los datos del cliente Middle con un array
		String[] datosLogin = DatosEmpresarial.getLoginData();
		// Reporta los datos del logeo
//		Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		if (contratacion.equals("SI") || contratacion.equals("SOLO"))
			Reporter.reportEvent(Reporter.MIC_INFO,
					"*** Datos de Logueo Middle: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		// numCli tipoDoc numDoc clave tipoTok datoTok
		numCliEmp = datosLogin[0];
		tipoDoc = datosLogin[1];
		numDoc = datosLogin[2];
		clave = datosLogin[3];
		tipoTok = datosLogin[4];
		datoTok = datosLogin[5];

// -----------------------------------------------------------------------------------------------------------------------
		// Guarda los Datos del cliente Middle
		PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
		// Guarda el Dato del Token Middle
		PageUsuariosEmpresa.datosMidellToke(datoTok);

// -----------------------------------------------------------------------------------------------------------------------

		String msgError = null;
		// PARÁMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		this.desde_el_Detalle = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();
		this.empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();

		String clienteEmpresarial = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		Idusuario = SettingsRun.getTestData().getParameter("Id usuario").trim();
		this.tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		this.tipoIDEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		this.numeroIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();

// -----------------------------------------------------------------------------------------------------------------------		

		/*
		 * Metodo se encarga de validar, que los datos obligatorios esten en el archivo
		 * a cargar
		 */
		// this.validarOCorregirData();

// -----------------------------------------------------------------------------------------------------------------------

		String msgInicio = "";

		if (contratacion.equals("SI") || contratacion.equals("SOLO")) {
			msgInicio = "*** Cliente Empresarial: [" + clienteEmpresarial + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Usuario: [" + tipoIdentificacion + "] - Tipo Identificación Usuario: ["
					+ Idusuario + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Servicio a contratar: [" + this.servicio + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Empresa a contratar: [" + this.empresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Empresa: [" + tipoIDEmpresa + "] - Numero Identificación Empresa: ["
					+ numeroIDEmpresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
		}

//		Inicializa los valores temporales.
//		Util para aquellos escenarios en que no se ha adicionado un registro, pero se deben ir guardando determinados datos: Fecha y hora de la transacciÓn /Resultado / Monto / Observación / Número de transacción.
		if (realizarMR.equals("SI")) {
			if (!this.riesgoBc.isEmpty() && realizarMR.equals("SI")
					|| !this.riesgoEfm.isEmpty() && realizarMR.equals("SI"))
				DatosDavivienda.RISKMOTOR.inicializaValoresTemp();
		}
		if (this.tipoPrueba.isEmpty() || this.tipoPrueba.equals(" ")) {
			Reporter.write("Falta Ingresar Datos de Tipo Prueba, Campo es Obligatorio ");
			SettingsRun.exitTestIteration();
		}

		if (this.servicio.contains("Tx Internacionales Recibir desde el exterior")
				|| this.servicio.contains("Tx Internacionales Enviar al exterior"))
			validarCliente = SettingsRun.getTestData().getParameter("ValidarC360").trim();
// -----------------------------------------------------------------------------------------------------------------------

//*************************************************************************************************************************
		/*
		 * OPCIÓN SI DESEAS REALIZAR LA CONTRATACIÓN, 1RE REALIZA EL FLUJO DE
		 * CONTRATACIÓN DESDE MIDDLE Y PROCEDE A REALIZAR EL FLIJO EN FRONT
		 */
		if (contratacion.equals("SI")) {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT

// -----------------------------------------------------------------------------------------------------------------------

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------

				this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int numfirm = Integer.parseInt(this.numAprobaciones);
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */
				if (!unaFirma) {
					// REALIZA EL BUCLE CON LA CANTIDA DE FIRMAS A PARAMETRIZAR
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
					do {

						if (contador != numfirm) { // AUMENTA EL VALOR DE LA VARIABLE
							contador++;
						}
						/*
						 * REALIZA EL FLUJO DE PYME MIDDLE, PARA REALIZAR LA CONTRARACION DE LOS
						 * SERVICIOS A LOS DEMAS USUARIOS SI ES POR FIRMAS, SUMA 1 A LA VARIABLE
						 * CONTADOR
						 */
						controllerValiPymeMiddle.ValidacionMiddlefirmas(contador);

						// EL BUCLE CONTINÚA MIENTRAS EL CONTADOR VARIABLE SEA MENOR QUE NUMFIRM.
					} while (contador < numfirm);

				} else {
// -----------------------------------------------------------------------------------------------------------------------
					// REALIZA LA CONTRATACI�N, EL FLUJO DE PYME MIDDLE
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				}

				// Realiza la validacion de cobros
				if (cobros.equals("SI")) {

					this.controllerMiddleCobros = new ControllerMiddleCobros(this.pageLogin);
					resultsMiddle = this.controllerMiddleCobros.consultarCombos();

				}
// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();

				// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {
					if (cobros.equals("SI")) {
						if (realizarMR.equals("NO")) {
							this.LoginStratus();
//							HashMap<String, String[]> cobros = DatosDavivienda.STRATUS.irTablaValores("PYMES");
						}
					}
					// LLAMA EL METODO DEL FLUJO LOGIN FRONT
					this.Front();
				}
			}
		}
//*************************************************************************************************************************		

//*************************************************************************************************************************	
		// OPCI�N SI SELECIONA "Solo Contratar servicios" SOLO REALIZA EL FLUJO DE
		// MIDDLE
		if (contratacion.equals("SOLO")) {

			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT
// -----------------------------------------------------------------------------------------------------------------------
			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {
				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);

// -----------------------------------------------------------------------------------------------------------------------				
				// NUMEROS DE FIRMAS A RALIZAR
				this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACI�N DE MIDDLE AL 2 CLIENTE
				 */
				boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int numfirm = Integer.parseInt(this.numAprobaciones);
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACI�N DE
				 * MIDDLE AL 2 CLIENTE
				 */
				if (!unaFirma) {
					// REALIZA EL BUCLE CON LA CANTIDA DE FIRMAS A PARAMETRIZAR
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
					do {
						if (contador != numfirm) {
							// AUMENTA EL VALOR DE LA VARIABLE
							contador++;
						}
						/*
						 * REALIZA EL FLUJO DE PYME MIDDLE, PARA REALIZAR LA CONTRARACION DE LOS
						 * SERVICIOS A LOS DEMAS USUARIOS SI ES POR FIRMAS, SUMA 1 A LA VARIABLE
						 * CONTADOR
						 */
						controllerValiPymeMiddle.ValidacionMiddlefirmas(contador);
						// EL BUCLE CONTIN�A MIENTRAS EL CONTADOR VARIABLE SEA MENOR QUE NUMFIRM.
					} while (contador < numfirm);
				} else {
// -----------------------------------------------------------------------------------------------------------------------
					// REALIZA EL FLUJO DE PYME MIDDLE
					controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				}
// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();
			}
// -----------------------------------------------------------------------------------------------------------------------
		}
//*************************************************************************************************************************

//*************************************************************************************************************************
		// -----------------------------------------------------------------------------------------------------------------------
		// OPCI�N SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
		if (contratacion.equals("NO")) {

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				// Valida si la opcion de MR si esta en No sigue con la validaciones de Cobro si
				// esta en si Para la ejecuci�n
				if (realizarMR.equals("NO")) {
					// Realiza la validacion de cobros
					if (cobros.equals("SI")) {
						this.LoginStratus();
//						HashMap<String, String[]> cobros = DatosDavivienda.STRATUS.irTablaValores("PYMES");

						Reporter.reportEvent(Reporter.MIC_INFO,
								"*** Datos de Logueo Middle: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");

						msgInicio = "*** Cliente Empresarial: [" + clienteEmpresarial + "]";
						Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
						msgInicio = "*** Tipo Identificación Usuario: [" + tipoIdentificacion
								+ "] - Tipo Identificación Usuario: [" + Idusuario + "]";
						Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
						msgInicio = "*** Tipo Servicio a contratar: [" + this.servicio + "]";
						Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
						msgInicio = "*** Empresa a contratar: [" + this.empresa + "]";
						Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
						msgInicio = "*** Tipo Identificación Empresa: [" + tipoIDEmpresa
								+ "] - Numero Identificación Empresa: [" + numeroIDEmpresa + "]";
						Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);

						String combo = SettingsRun.getTestData().getParameter("Combos");
						String tipoCobro = SettingsRun.getTestData().getParameter("Tipo Prueba Cobros");

						Reporter.reportEvent(Reporter.MIC_INFO, "*** COMBO SELECIONADO: " + combo);
						Reporter.reportEvent(Reporter.MIC_INFO, "*** TIPO COBRO SELECIONADO: " + tipoCobro);
// --------------------------------------------------------------------------------------------------------------------------------------
						// INTENTA HACER EL LOGUEO
						this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la
																			// prueba
						msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal Middle
																	// Pyme.
						this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta
																	// en
																	// el
																	// Ambiente Middle o FRONT
						this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);

						this.controllerMiddleCobros = new ControllerMiddleCobros(this.pageLogin);
						resultsMiddle = this.controllerMiddleCobros.consultarCombos();
						Reporter.write(resultsMiddle);

						// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
						this.pageLogin.CerrarSesionMiddle();
						// Ingresa a consultar los Segmentos obtenido desde middle
//						segmentosStratus = DatosDavivienda.STRATUS.irTablaValores(resultsMiddle.get(0).get("Segmento"));

					}

					// Termina la prueba ya que no tiene que validar Middle
				} else if (realizarMR.equals("SI")) {

				}

			}

			this.Front();
		}

// -----------------------------------------------------------------------------------------------------------------------

		if (contratacion == "Informe") {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT

// -----------------------------------------------------------------------------------------------------------------------

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------
				// NUMEROS DE FIRMAS A RALIZAR
				this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

				controllerValiPymeMiddle.ValidacionInformeTransInternacional();

			}
// -----------------------------------------------------------------------------------------------------------------------
			// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
//				this.pageLogin.CerrarSesionMiddle();

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN

		}
// -----------------------------------------------------------------------------------------------------------------------
//*************************************************************************************************************************
	}
// =======================================================================================================================	

// ===========================================FRONT===========================================================================
	/**
	 * Método que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */

	public void Front() throws Exception {

		switch (this.servicio) {

		case "Nómina":
		case "Pago de Nómina":
		case "Pago de Nóminas":
		case "Pago a Proveedores":
		case "Pagos a proveedores":
		case "Pagos proveedores":
		case "Proveedores":
		case "AFC":
		case "Pago a Créditos de Terceros":
		case "Pagos a créditos de terceros":
		case "Crédito.3ros":
			// Obtiene la ruta del archivo de Destino desde las configuraciones
			String archivoDest = SettingsRun.getTestData().getParameter("Archivo Destinos").trim();
			// Verifica si el archivo existe
			if (!DXCUtil.ArchivoExist(archivoDest)) {
				Reporter.reportEvent(Reporter.MIC_NOEXEC,
						"El sistema no puede encontrar el archivo destino especificado: " + archivoDest);
				// Cierra todas las ventanas abiertas y sale de la iteración del test si es
				// necesario
				if (this.pageLogin != null)
					if (this.pageLogin.ThereareOpenWindows())
						this.pageLogin.closeAllBrowsers();
				SettingsRun.exitTestIteration();
			}
			break;
		case "Tx Internacionales Recibir desde el exterior":
		case "Tx Internacionales Enviar al exterior":
		case "Tx Internacionales Enviar al exterior Pendiente Aprobación":
		case "Divisas Documentos y Formularios":
		case "Consulta Tx Internacionales Enviar al exterior Validar Estado":

			// Obtiene la ruta del archivo de documentos desde las configuraciones
			String cargueDocu = SettingsRun.getTestData().getParameter("Cargue Archivo Documentos");

			// Divide la ruta en un array de strings separados por comas
			String[] rutaArch = cargueDocu.split(",");
			boolean archivoExiste = true;

			for (String nombreArch : rutaArch) {
				// Verifica si el archivo existe
				if (!DXCUtil.ArchivoExist(nombreArch)) {
					// Si el archivo no existe, reporta el evento
					Reporter.reportEvent(Reporter.MIC_INFO,
							"El sistema no puede encontrar el archivo Cargue de documento especificado: " + nombreArch);
					archivoExiste = false;
				}
			}

			if (!archivoExiste) {
				Reporter.reportEvent(Reporter.MIC_NOEXEC,
						"El sistema no puede encontrar el archivo Cargue especificado: " + cargueDocu);
				// Cierra todas las ventanas abiertas y sale de la iteración del test si es
				// necesario
				if (this.pageLogin != null)
					if (this.pageLogin.ThereareOpenWindows())
						this.pageLogin.closeAllBrowsers();
				SettingsRun.exitTestIteration();
			}
			break;

		default:
			break;
		}

		if (realizarMR.equals("NO")) {
			// Realiza el Login en Stratus
			this.LoginStratus();
			if (cobros.equals("SI")) {
				if (DXCUtil.ArchivoExist(SettingsRun.getParentResultDir() + "Archivo_Segmentos.txt") && DXCUtil
						.archivoCreadoOModificadoHoy(SettingsRun.getParentResultDir() + "Archivo_Segmentos.txt")) {
					if (this.controllerDestinosMasivos == null)
						this.controllerDestinosMasivos = new ControllerDestinosMasivos(null);
					segmentosStratus = this.controllerDestinosMasivos.LeerArchivoPlano();
				} else {
					// Ingresa a consultar los Segmentos obtenido desde middle
					Reporter.reportEvent(Reporter.MIC_INFO, "Validacion Segmento en Stratus");
					segmentosStratus = DatosDavivienda.STRATUS.irTablaValores(resultsMiddle.get(0).get("Segmento"));
					if (this.controllerDestinosMasivos == null)
						this.controllerDestinosMasivos = new ControllerDestinosMasivos(null);
					this.controllerDestinosMasivos.CrearArchivoPlano_Segmentos(segmentosStratus);

				}

			}
		}

		DXCUtil.Movercursor();
// -----------------------------------------------------------------------------------------------------------------------	
		String msgError = null;
		int intento = 0;
		String msgInicio = "";
		String msgInicio2 = "";
// -----------------------------------------------------------------------------------------------------------------------
		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación", "Id usuario",
				"Clave personal o CVE", "Tipo Token", "Semilla / Valor Estático / Celular");

		String[] datosLogin = DatosEmpresarial.getLoginData();

		Reporter.reportEvent(Reporter.MIC_INFO, "*** Navegador: [" + this.navegador + "]");
		Reporter.reportEvent(Reporter.MIC_INFO,
				"*** Datos de Logueo Front: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
		Reporter.reportEvent(Reporter.MIC_INFO, "*** Números de firmas: [" + this.numAprobaciones + "]");
		if (realizarMR.equals("SI")) {
			// NIVEL DE RIESGO DE LA PRUEBA MOTOR
			if (!this.riesgoBc.isEmpty() && realizarMR.equals("SI")
					|| !this.riesgoEfm.isEmpty() && realizarMR.equals("SI")) {
				if (SettingsRun.getGlobalData("MOTOR.tipoMotor").contains("EFM")) {
					msgInicio = "*** Tipo de Motor de riesgo : [SAS " + SettingsRun.getGlobalData("MOTOR.tipoMotor")
							+ "]";
				} else {
					msgInicio = "*** Motor de riesgo : [" + SettingsRun.getGlobalData("tipoMotor") + "]";
				}
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
				msgInicio = "*** Riesgo BC: [" + riesgoBc + "]";
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
				if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
						&& !this.servicio.equals("Tx Internacionales Enviar al exterior")) {
					msgInicio = "*** Riesgo EFM: [" + riesgoEfm + "]";
					Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
				}
			}
		}

		// INFORMA EL TIPO DE PRUEBA
		if (tipoPrueba.equals(TP_EN_LINEA) || tipoPrueba.equals(TP_PEND_APR)) {
			msgInicio2 = "*** Tipo Prueba: [" + this.tipoPrueba + "]";
			if (!contratacion.equals("SOLO"))
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio2);

			msgInicio2 = "*** Tipo transaccion: [" + this.servicio + "]";
			if (!contratacion.equals("SOLO"))
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio2);

			msgInicio2 = "*** Empresa: [" + this.empresa + "]";
			if (!contratacion.equals("SOLO"))
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio2);

			msgInicio2 = "*** Aprobar Desde el detalle: [" + this.desde_el_Detalle + "]";
			if (!contratacion.equals("SOLO"))
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio2);
		}

		if (tipoPrueba.equals(TP_LOGIN)) {
			msgInicio2 += " " + "Tipo Prueba: " + this.tipoPrueba;
			if (!contratacion.equals("SOLO"))
				Reporter.reportEvent(Reporter.MIC_INFO, msgInicio2);
//						Reporter.write(msgInicio);
		}
// ***********************************************************************************************************************

		do {

// -----------------------------------------------------------------------------------------------------------------------			
			// INTENTA HACER EL LOGUEO
			if (this.servicio.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado")) {

				this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());
			} else {
				this.pageLogin = new PageLoginPymes(this.navegador);

			}
//			DXCUtil.ApiGet();
			msgError = this.pageLogin.loginFront();// M�todo para hacer el logueo en el portal front Pyme.
			fechaHoraLogMR = this.pageLogin.getFechaHoraLogMR();
			this.pageLogin.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT

			if (DatosDavivienda.IS_RISKMOTOR && msgError != null && !this.tipoPrueba.equals("Login")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, " *** No se adiciona a MR >>>" + msgError);
			}
// -----------------------------------------------------------------------------------------------------------------------
			// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;

			}
// -----------------------------------------------------------------------------------------------------------------------
			/*
			 * SI EL ACCESO ES DENEGADO POR SER RIESGO ALTO SE ESCRIBE EN EL ARCHIVO DE
			 * SALIDA SI EL ACCESO ES DENEGADO PERO ES RIEGO BAJO O MEDIO ESCRIBE ERROR EN
			 * EL ARCHIVO DE SALIDA
			 */

// -----------------------------------------------------------------------------------------------------------------------			

			this.tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();

			if (realizarMR.equals("SI")) {

				if (!this.riesgoBc.isEmpty() || !this.riesgoEfm.isEmpty()) {
					if (this.tipoPrueba.contains("Login")) {
						if (msgError != null && msgError.contains("Acceso denegado.En un momento")) {
							DatosDavivienda.RISKMOTOR.setTemporalTime(this.pageLogin.getFechaHoraLogMR());
							DatosDavivienda.RISKMOTOR.setTemporalMonto("0");

							// this.transaccion = "Login Declinado";

							if (msgError.contains("Acceso denegado.En un momento")) {
								if (this.riesgo.equals("Bajo") || this.riesgo.equals("Medio")) {
									Reporter.reportEvent(Reporter.MIC_FAIL, "Se esperaba Ingreso al portal");
								}
								this.adicionarRegistroMR("Error en Login");

								this.pageLogin.closeAllBrowsers();
								SettingsRun.exitTestIteration();

							} else {
								if (this.riesgo.equals("Alto")) {
									Reporter.reportEvent(Reporter.MIC_FAIL, "Se esperaba Acceso Denegado");
								}
								this.adicionarRegistroMR("Login");
							}

//							DatosDavivienda.RISKMOTOR.setCausaRechazo("Acceso denegado.En un momento un asesor lo contactará.");
							DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");

							intento++;

							this.pageLogin.closeAllBrowsers();
							SettingsRun.exitTestIteration();
						}

					} else if (this.tipoPrueba.contains("Tx En Línea")
							|| this.tipoPrueba.contains("Tx Pend Aprobación")) {
						if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")
								|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {

						}
//						else {
//
//							this.adicionarRegistroMR(this.tipoPrueba);
//						}
					}
				} else if (!this.riesgoBc.isEmpty()) {
					Reporter.write("Falta Ingresar Datos de MR, Campos Nivel de Riesgo BC ");
					this.pageLogin.closeAllBrowsers();
					SettingsRun.exitTestIteration();

				} else if (!this.riesgoEfm.isEmpty()) {
					Reporter.write("Falta Ingresar Datos de MR, Campos Nivel de Riesgo BC ");
					this.pageLogin.closeAllBrowsers();
					SettingsRun.exitTestIteration();
				}

			}
// -----------------------------------------------------------------------------------------------------------------------

		} while (this.pageLogin.element(log30) != null && intento < 3);

// ***********************************************************************************************************************

		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {
//			 HACE EL PROCESO PARA TIPO DE PRUEBA "TRANSACCION EN LINEA" O "TRANSACCION PENDIENTE DE APROBACI�N"
			if (realizarMR.equals("SI") && tipoPrueba.equals(TP_EN_LINEA)
					|| realizarMR.equals("SI") && tipoPrueba.equals(TP_PEND_APR)) {
				if (!this.riesgoBc.isEmpty() || !this.riesgoEfm.isEmpty()) {
					DatosDavivienda.RISKMOTOR.setTemporalTime(this.pageLogin.getFechaHoraLogMR());
					DatosDavivienda.RISKMOTOR.setTemporalMonto("0");

//					if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {
//					} else {
//						this.controller.validarIngresoMR();
//					}

//					if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")|| !this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {
//					} else {
//
//						DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(this.navegador);
//					}
				}
			}

			// Realiza la busqueda de La ip
//			if (realizarMR.equals("SI")) {
//				this.pageLogin.navigate(BaseTestNG.getProperties().getProperty("IP.PUBLICA"));
//				do {
//					DXCUtil.wait(1);
//
//				} while (this.pageLogin.element(ip) == null);
//				String ipP = this.pageLogin.getText(ip);
//				ipP = DXCUtil.right(ipP, 18);
//				this.ipPublica = DXCUtil.left(ipP, 15);
//			}

// Cierra la ventana actual del login de Front ya que va interatuar con la ventana emergente

			DXCUtil.wait(5);

			boolean isWindowOpened = this.pageLogin.WaitForNumberOfWindos();
			if (isWindowOpened) {
				Reporter.reportEvent(Reporter.MIC_PASS, "La ventana emergente se abrió correctamente");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se abrió La ventana emergente");
				this.pageOrigen.terminarIteracion();
			}

			this.pageLogin.closeCurrentBrowser();

// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());

			this.pageLogin.maximizeBrowser();
//			 HACE EL PROCESO PARA TIPO DE PRUEBA "TRANSACCION EN LINEA" O "TRANSACCION PENDIENTE DE APROBACI�N"
			if (msgError == null && tipoPrueba.equals(TP_EN_LINEA)
					|| msgError == null && tipoPrueba.equals(TP_PEND_APR)) {
// -----------------------------------------------------------------------------------------------------------------------
				// Obtiene el nombre de la empresa a selecionar

				this.pageOrigen = new PageOrigen(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------

				/*
				 * Intenta seleccionar la empresa. Retorna [null] si pudo hacer la selecci�n, en
				 * caso contrario retorna mensajede error.
				 */

				msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
				// SI ES NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEM�S VALIDACIONES
				if (msgError == null) {

// -----------------------------------------------------------------------------------------------------------------------

					// Datos requeridos para la configuracion de los datos generales

					String tipoAbono = SettingsRun.getTestData().getParameter("Tipo de abono").trim();
					String ctaInscrita = SettingsRun.getTestData().getParameter("Cuentas Inscriptas").trim();
					this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
					String Idusuario = SettingsRun.getTestData().getParameter("Id usuario").trim();
// -----------------------------------------------------------------------------------------------------------------------

					// Comparar con los valores anteriores
					if (!Idusuario.equals(lastIdusuario) || !this.empresa.equals(lastempresa)
							|| !numAprobaciones.equals(lastNumAprobaciones) || !tipoAbono.equals(lastTipoAbono)
							|| !ctaInscrita.equals(lastCtaInscrita)) {

						if (!this.servicio.equals("Divisas Documentos y Formularios") && !this.servicio
								.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado")) {
							// Realizar la configuraci�n si los valores son diferentes
							this.pageAdminParametros = new PageAdminParametros(this.pageLogin);
							// Este m�todo hace la configuraci�n en par�metros generales, y guarda la
							// evidencia.
							msgError = this.pageAdminParametros.hacerConfiguracion(numAprobaciones, tipoAbono,
									ctaInscrita);
						} else {
							msgError = "Divisas";
						}

						// Actualizar los valores
						lastNumAprobaciones = numAprobaciones;
						lastTipoAbono = tipoAbono;
						lastCtaInscrita = ctaInscrita;
						lastIdusuario = Idusuario;
						lastempresa = this.empresa;

					} else {
						// Omitir la configuraci�n si los valores son los mismos
						msgError = "Ya se configuro los Parámetros Generales";
					}

					String codigoCIIU = "";
					if (this.servicio.contains("Tx Internacionales Recibir desde el exterior")
							|| this.servicio.contains("Tx Internacionales Enviar al exterior")) {
						codigoCIIU = SettingsRun.getTestData().getParameter("Validar CIIU").trim();

						if (codigoCIIU.equals("SI")) {
							DXCUtil.wait(3);
							this.pageActualizacionDeDatos = new PageActualizacionDeDatos(this.pageLogin);

							msgError = this.pageActualizacionDeDatos.InicioActualizacionDatos(false);

							if (msgError != null
									&& !msgError.equals("Se actualizaron exitosamente los datos de su empresa")) {
								msgError = this.pageActualizacionDeDatos.MsgAlertaActualizacionDatos();
								this.pageOrigen.terminarIteracion(Reporter.MIC_FAIL, msgError);
							} else {
								Reporter.reportEvent(Reporter.MIC_PASS, msgError);
							}
						}

						if (validarCliente.equals("SI")) {

							this.pageInicioC360 = this.logueoC360();
							this.pageConsultatxInternacional = new PageConsultatxInternacional(this.pageInicioC360);
							this.pageConsultatxInternacional.ValidarCCIU();
							Reporter.write("*** Termina la Validación CLIENTE 360 ***");
						}

					}

					// SI ES INDIFRENTE A NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEM�S
					// VALIDACIONES
					if (msgError != null) {

// -----------------------------------------------------------------------------------------------------------------------

						this.transar(); // Se encuentra logueado en el portal Pymes, empieza arealizar las TX.

					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
						this.pageOrigen.terminarIteracion();

					}
				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
					this.pageOrigen.terminarIteracion();
				}

				// HACE EL PROCESO PARA TIPO DE PRUEBA "LOGIN"
			} else if (msgError == null && tipoPrueba.equals(TP_LOGIN)) {

// -----------------------------------------------------------------------------------------------------------------------

				do {

					// SE VALIDA VARIOS LOCATOR ANTES DE CERRAR SESION, POR INESTABILIDAD EN EL
					// AMBIENTE
					boolean imgInicial = this.pageLogin.isDisplayed(cmCerrSes);

					imgInicial = this.pageLogin.isDisplayed(cerrarSesion);

					imgInicial = this.pageLogin.isDisplayed(fecha);

					DXCUtil.wait(1);

				} while (this.pageLogin.element(fecha) == null);

				// Selecciona cerrar sesion
				if (this.pageLogin.isDisplayed(cerrarSesion) && this.pageLogin.isDisplayed(fecha)
						&& this.pageLogin.isDisplayed(cmCerrSes)) {
					Evidence.save("Incio de sesion pantalla principal");
					String scriptClicIngresar1 = "document.querySelector('a[id=\"CerrarSesion\"').click();";
					this.pageLogin.getJse().executeScript(scriptClicIngresar1);
//							DatosDavivienda.RISKMOTOR.setTemporalResultado(MotorRiesgo.RESULT_SUCCESS);

					if (realizarMR.equals("SI") && this.riesgo.equals("Alto")) {
						DatosDavivienda.RISKMOTOR.setTemporalResultado("Exitosa");
						this.adicionarRegistroMR("Error en Login");
					} else if (realizarMR.equals("SI")) {
						DatosDavivienda.RISKMOTOR.setTemporalResultado("Exitosa");
						this.adicionarRegistroMR("Login");
					}
				}

			}
//			else {
//				Reporter.reportEvent(Reporter.MIC_FAIL, " " + msgError);
//				this.pageOrigen.terminarIteracion();
//			}
			this.pageLogin.closeAllBrowsers();
		}

	}

// =======================================================================================================================

// ===========================================[FRONT DOBLE FIRMA]===========================================================================
	/**
	 * M�todo que llama logueo en el portal Front Pyme si es diferente a una firma.
	 * Realiza el logueo con el usuario a realizar la aprobaci�n
	 */
	public void Frontfirmas(int firmas) throws Exception {
		String msgError = null;
		int intento = 0;

		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación " + firmas, "Id usuario " + firmas,
				"Clave personal o CVE " + firmas, "Tipo Token " + firmas,
				"Semilla / Valor Estático / Celular " + firmas);

//-----------------------------------------------------------------------------------------------------------------------
		do {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);
			msgError = this.pageLogin.loginFrontFirmas();// M�todo para hacer el logueo en el portal front Pyme.
			this.pageLogin.selecionambienteClose("NO");// Indicativo para el ambiente Front// Marca si esta en el
														// Ambiente Middle o FRONT
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;
			}
		} while (this.pageLogin.element(log30) != null && intento < 2);

// -----------------------------------------------------------------------------------------------------------------------
		// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
		if (msgError == null) {

			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente

			DXCUtil.wait(7);

			boolean isWindowOpened = this.pageLogin.WaitForNumberOfWindos();
			if (isWindowOpened) {
				Reporter.reportEvent(Reporter.MIC_PASS, "La ventana emergente se abrió correctamente");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se abrió La ventana emergente");
				this.pageOrigen.terminarIteracion();
			}

			this.pageLogin.closeCurrentBrowser();

			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
			this.pageLogin.maximizeBrowser();
// ----------------------------------------------------------------------------------------------------------------------- 
			// Valida la empresa al momento de ingresar en FRONT

			this.pageOrigen = new PageOrigen(this.pageLogin);

			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);

			if (msgError != null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageOrigen.terminarIteracion();
			}
		}
	}

// =======================================================================================================================		

// ===========================================TRANSACCIONAL===========================================================================
	/**
	 * Se encuentra logueado en el portal Pymes, se intenta realizar la transacci�n
	 * requerida. Realiza la tx dependiendo si es transacion "Tx En L�nea" o "Tx
	 * Pend Aprobaci�n". Donde realiza la probacion de una vez o primero guarda sin
	 * aprobar, y des pues aprueba la tx, en el modulo de pendiente de aprobaci�n
	 * 
	 */
	public void transar() throws Exception {
		this.controller = new ControllerCrearTx(this.pageLogin);
		// -----------------------------------------------------------------------------------------------------------------------

		if (realizarMR.equals("SI")) {
			this.controller.SetPrioridaMr(this.riesgo);
			String scriptClicIngresar1 = "return navigator.userAgent;";
			this.userAgent = (String) this.pageLogin.getJse().executeScript(scriptClicIngresar1);
//			this.controller.userAgent(this.userAgent);
		}
// -----------------------------------------------------------------------------------------------------------------------
		// Compara si es igual a "Tx Pend Aprobaci�n"
		// si es primeroGuardar es true si es !primeroGuardar false
		boolean primeroGuardar = this.tipoPrueba.equals(TP_PEND_APR);

		switch (this.servicio) {

		case "Nómina":
		case "Pago de Nómina":
		case "Pago de Nóminas":
		case "Pago a Proveedores":
		case "Pagos a proveedores":
		case "Pagos proveedores":
		case "Proveedores":
		case "AFC":
		case "Pago a Créditos de Terceros":
		case "Pagos a créditos de terceros":
		case "Crédito.3ros":
			this.controller.crearPagoNomina_Proveedores(primeroGuardar);
			break;
		case "Pago de Servicios":
		case "Servicios":
			this.controller.crearPagoServicios(primeroGuardar);
			break;
		case "Pagos Automaticos":
			this.controller.crearPagosAutomaticos(primeroGuardar);
			break;
		case "Transferencias Mismo NIT":
		case "Transferencia NIT Propio":
		case "Mismo NIT":
			this.controller.crearTranferenciaMismoNit(primeroGuardar);
			break;
		case "Transferencias Cuenta Inscrita":
		case "Cuenta Inscrita":
			this.controller.crearTransferenciaCtaInscrita(primeroGuardar);
			break;
		case "Transferencias Cuenta No Inscrita":
		case "Transferencias Cuenta NO Inscrita":
		case "Cuenta No Inscrita":
			this.controller.crearTransferenciaCtaNoInscrita(primeroGuardar);
			break;
		case "ORPA":
			this.controller.OrdenesDePago();
			break;
		case "Pagos Propios":
			this.controller.crearPagosPropios(primeroGuardar);
			break;
		case "Avances Tarjeta de Crédito":
		case "Avance TC":
			this.controller.crearAvanceTC(primeroGuardar);
			break;

		case "Tx Internacionales Recibir desde el exterior":
//			SettingsRun.getTestData().addParametersNotExist("Ordenante", "Monto", "Tipo de cambio a USD","Monto en USD", "Tasa de cambio", "Valor de la operación en pesos colombianos",
//					"Costo del servicio", "IVA sobre costo del servicio", "Valor total a descontar de la cuenta",
//					"Valor Neto a recibir", "Tipo de transferencia", "Fecha tx", "Hora tx", "Estado",
//					"Nombre de Usuario", "Número Aprobación");

			this.controller.Recibirdinerodelexterior(primeroGuardar);

			break;

		case "Tx Internacionales Enviar al exterior":
//			SettingsRun.getTestData().addParametersNotExist("Ordenante", "Monto", "Tipo de cambio a USD",
//					"Monto en USD", "Tasa de cambio", "Valor de la operación en pesos colombianos",
//					"Costo del servicio", "IVA sobre costo del servicio", "Valor total a descontar de la cuenta",
//					"Valor Neto a recibir", "Tipo de transferencia", "Fecha tx", "Hora tx", "Estado",
//					"Nombre de Usuario", "Número Aprobación");

			this.controller.EnviarTransferenciasInternacionales(primeroGuardar);
			break;

		case "Tx Internacionales Enviar al exterior Pendiente Aprobación":

//			SettingsRun.getTestData().addParametersNotExist("Ordenante", "Monto", "Tipo de cambio a USD",
//					"Monto en USD", "Tasa de cambio", "Valor de la operación en pesos colombianos",
//					"Costo del servicio", "IVA sobre costo del servicio", "Valor total a descontar de la cuenta",
//					"Valor Neto a recibir", "Tipo de transferencia", "Fecha tx", "Hora tx", "Estado",
//					"Nombre de Usuario", "Número Aprobación");

			this.controller.inicioCrearTx();
			this.controller.EnviarTransferenciasInternacionalesPendAprobacion();

			break;
		case "Divisas Documentos y Formularios":

			String msg = null;

//			SettingsRun.getTestData().addParametersNotExist("Ordenante", "Monto", "Tipo de cambio a USD",
//					"Monto en USD", "Tasa de cambio", "Valor de la operación en pesos colombianos",
//					"Costo del servicio", "IVA sobre costo del servicio", "Valor total a descontar de la cuenta",
//					"Valor Neto a recibir", "Tipo de transferencia", "Fecha tx", "Hora tx", "Estado",
//					"Nombre de Usuario", "Número Aprobación");

			this.controller.inicioCrearTx();

			this.pageDocumentos_Y_Formularios = new PageDocumentos_Y_Formularios(this.pageLogin);

			this.pageDivisas = new PageDivisas(this.pageLogin);

			if (this.pageDivisas.switchToFrameDivisas()) {

				msg = this.pageDocumentos_Y_Formularios.IralModuloDocumetosYFormularios();

				if (msg != null) {
					if (!msg.isEmpty())
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.pageOrigen.terminarIteracion();
				}

				// Datos iniciales
				String concepTx = SettingsRun.getTestData().getParameter("Concepto de la transferencia").trim();
				String numCambiario1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"), 4);
				String valorNumeral1 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 1");
				String numCambiario2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"), 4);
				String valorNumeral2 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 2");
				String valorTx = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir").trim();

				// Datos Documentos y formularios
				String tipoOperacion = SettingsRun.getTestData().getParameter("Tipo de operación");
				String desInversion = SettingsRun.getTestData().getParameter("Destino de la inversión");
				String opciondeinversion = SettingsRun.getTestData().getParameter("Opción de inversión");
				String deducciones = SettingsRun.getTestData().getParameter("Deducciones");
				String cargueDocu = SettingsRun.getTestData().getParameter("Cargue Archivo Documentos");

				// Divide la ruta en un array de strings separados por comas
				String[] rutaArch = cargueDocu.split(",");
				msg = this.pageDocumentos_Y_Formularios.DatosDocumetosYFormularios(concepTx, tipoOperacion,
						desInversion, opciondeinversion, valorTx, numCambiario1, valorNumeral1, numCambiario2,
						valorNumeral2, deducciones, rutaArch);

				if (msg != null && !msg.contains(
						"Documentos enviados exitosamente. Davivienda validará la información recibida y en caso de presentar inconsistencias informará vía correo electrónico. Por favor haga seguimiento de su operación en la opción de consultas y verifique el estado de su trámite.")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.pageOrigen.terminarIteracion();
				}
			}

			break;
		case "Consulta Tx Internacionales Validar Estado":

//			SettingsRun.getTestData().addParametersNotExist("Ordenante", "Monto", "Tipo de cambio a USD",
//					"Monto en USD", "Tasa de cambio", "Valor de la operación en pesos colombianos",
//					"Costo del servicio", "IVA sobre costo del servicio", "Valor total a descontar de la cuenta",
//					"Valor Neto a recibir", "Tipo de transferencia", "Fecha tx", "Hora tx", "Estado",
//					"Nombre de Usuario", "Número Aprobación");

			this.controller.inicioCrearTx();

			this.pageConsultatxInternacional = new PageConsultatxInternacional(this.pageLogin);
			this.pageConsultatxInternacional.ConsultaNumtx(this.servicio);

			break;
		}

		boolean desdeelDetalle = this.desde_el_Detalle.equals(DE_El_DETALLE);
// -----------------------------------------------------------------------------------------------------------------------
		// SI LLEG� A ESTE PUNTO, SE PUDO REALIZAR LA CREACI�N DE LA TRANSACCI�N
		// PRIMERO GUARD�, AHORA SE DEBE SOLICITAR APROBACI�N
		if (primeroGuardar && !this.servicio.contains("Internacionales")
				|| primeroGuardar && !this.servicio.contains("Internacionales") && DatosDavivienda.IS_RISKMOTOR)
			this.controller.aprobarTxPendiente(desdeelDetalle);

// -----------------------------------------------------------------------------------------------------------------------
		String msgTx = null;
		// SI LLEG� A ESTE PUNTO, SE PUDO REALIZAR LA CREACI�N DE LA TRANSACCI�N
		// PRIMERO GUARD�, AHORA SE DEBE SOLICITAR APROBACI�N PARA INTERNACIONAL
		if (primeroGuardar && this.servicio.contains("Internacionales") && !this.servicio.contains("Aprobación")
				|| primeroGuardar && this.servicio.contains("Internacionales") && DatosDavivienda.IS_RISKMOTOR) {
			msgTx = this.controller.aprobarTxPendienteIntern(desdeelDetalle);
		}

// -----------------------------------------------------------------------------------------------------------------------
		// SI LLEG� A ESTE PUNTO, SE PUDO REALIZAR LA APROBACION
		// Obtiene el n�mero de firmas
		this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
		this.informe = SettingsRun.getTestData().getParameter("Informes").trim();

		boolean unaFirma = this.numAprobaciones.equals(CN_APRO_PEND);

		this.pageConsultasyExtractos = new PageConsultasyExtractos(this.pageLogin);

		int numfirm = Integer.parseInt(this.numAprobaciones);
		int contador = 1;

		if (!unaFirma) {
			this.pageLogin.CerrarSesionFront();
			do {
				if (contador != numfirm) {
					contador++;
				}

				this.Frontfirmas(contador);

				if (!this.servicio.contains("Internacionales") && !this.servicio.contains("Pagos Automaticos")) {
					this.controller = new ControllerCrearTx(this.pageLogin);
					if (realizarMR.equals("SI")) {
						this.controller.SetPrioridaMr(this.riesgo);
					}

					DXCUtil.wait(10);
					this.controller.aprobarTxPendiente(desdeelDetalle);
					if (contador == numfirm) {
						this.controller.validacionSaldosStratus();
					}
					// SE UTILIZA CERRA SESION MIDDLE PARA CERRAR EL CHROME
					if (numfirm == contador) {
						this.pageConsultasyExtractos = new PageConsultasyExtractos(this.pageLogin);
						String tipoProduct = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia")
								.trim();
						String numeroProducto = SettingsRun.getTestData().getParameter("Número producto origen").trim();
						this.pageConsultasyExtractos.ConsultayExtractos(servicio, navegador, tipoIDEmpresa,
								numeroIDEmpresa, tipoIdentificacion, Idusuario, tipoProduct, numeroProducto,
								primeroGuardar);
					}

				} else if (this.servicio.contains("Pagos Automaticos")) {
					this.pageTransaccionesProgramadas = new PageTransaccionesProgramadas(this.pageLogin);
					this.pageTransaccionesProgramadas.ConsultaPagosAutomatico();

				} else if (this.servicio.contains("Internacionales")) {

					DXCUtil.wait(10);

					this.controller.aprobarTxPendienteIntern(primeroGuardar);

					if (numfirm == contador) {
						this.pageConsultatxInternacional = new PageConsultatxInternacional(this.pageLogin);
						this.pageConsultatxInternacional.ConsultaNumtx(this.servicio);
						this.pageConsultatxInternacional.getDriver().switchTo().defaultContent();
					}
				}

				this.pageLogin.CerrarSesionFront();
				if (contador == numfirm)

					if (DatosDavivienda.IS_RISKMOTOR) {
						if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")
								|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {
							this.controller.validarIngresoMRDestinosMasivo();
						} else {
							this.controller.validarIngresoMR();
						}
					}

			} while (contador < numfirm);

		} else if (unaFirma) {
			boolean internacionales = false;
			String estado = null;

			if ((!this.servicio.contains("Internacionales") && !this.servicio.contains("Divisas"))
					&& !this.servicio.contains("Pagos Automaticos") && !this.servicio.contains("ORPA")) {
				this.pageConsultasyExtractos = new PageConsultasyExtractos(this.pageLogin);

				String tipoProduct = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
				String numeroProducto = SettingsRun.getTestData().getParameter("Número producto origen").trim();

				estado = this.pageConsultasyExtractos.ConsultayExtractos(servicio, navegador, tipoIDEmpresa,
						numeroIDEmpresa, tipoIdentificacion, Idusuario, tipoProduct, numeroProducto, primeroGuardar);

				if (estado != null && estado.equals("NO SE ENCONTRARON DATOS")) {

					if (DatosDavivienda.IS_RISKMOTOR) {

						if (this.servicio.contains("Númina") || this.servicio.contains("Pago a Proveedores")
								|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {
							this.controller.validarIngresoMRDestinosMasivo();
						} else {

							this.controller.validarIngresoMR();

						}
					}

					this.pageOrigen.terminarIteracion();
				}
				estadotxFinal = this.pageConsultasyExtractos.estadoFinalTx();
				if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")
						|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros"))
					if (this.pageConsultasyExtractos.CuentaDesMotor() != null) {
						this.controller.cuentasDesMotor = this.pageConsultasyExtractos.CuentaDesMotor();
					}
			} else if (this.servicio.contains("Pagos Automaticos")) {
				this.pageTransaccionesProgramadas = new PageTransaccionesProgramadas(this.pageLogin);
				this.pageTransaccionesProgramadas.ConsultaPagosAutomatico();

			} else if ((this.servicio.contains("Internacionales") || this.servicio.contains("Divisas")) && !this.servicio.contains("Aprobación") && !this.servicio.contains("Validar")) {

//				if (this.tipoPrueba.equals(TP_PEND_APR)
////						&& msgTx !=null && msgTx.contains("Documentaci�n requerida")
//				) {
//					this.pageDocumentos_Y_Formularios = new PageDocumentos_Y_Formularios(this.pageLogin);
//
//					String msg = null;
//					msg = this.pageDocumentos_Y_Formularios.IralModuloDocumetosYFormularios();
//
//					if (!msg.isEmpty()) {
//						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
//						this.pageOrigen.terminarIteracion();
//					}
//
//					// Datos iniciales
//					String concepTx = SettingsRun.getTestData().getParameter("Concepto de la transferencia").trim();
//					String numCambiario1 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 1"),4);
//					String valorNumeral1 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 1");
//					String numCambiario2 = DXCUtil.left(SettingsRun.getTestData().getParameter("Numeral cambiario 2"),4);
//					String valorNumeral2 = SettingsRun.getTestData().getParameter("Valor numeral cambiario 2");
//					String valorTx = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir").trim();
//
//					// Datos Documentos y formularios
//					String tipoOperacion = SettingsRun.getTestData().getParameter("Tipo de operación");
//					String desInversion = SettingsRun.getTestData().getParameter("Destino de la inversión");
//					String opciondeinversion = SettingsRun.getTestData().getParameter("Opción de inversión");
//					String deducciones = SettingsRun.getTestData().getParameter("Deducciones");
//					String cargueDocu = SettingsRun.getTestData().getParameter("Cargue Archivo Documentos");
//					// Divide la ruta en un array de strings separados por comas
//					String[] rutaArch = cargueDocu.split(",");
//					
//					msg = this.pageDocumentos_Y_Formularios.DatosDocumetosYFormularios(concepTx, tipoOperacion,desInversion, opciondeinversion, valorTx, numCambiario1, valorNumeral1, numCambiario2,
//							valorNumeral2, deducciones, rutaArch);
//
//					if (msg != null) {
//						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
//						this.pageOrigen.terminarIteracion();
//					}
//
//				}

				this.pageConsultatxInternacional = new PageConsultatxInternacional(this.pageLogin);
				this.pageConsultatxInternacional.ConsultaNumtx(this.servicio);

				if (stratus.equals("SI"))
					this.pageConsultatxInternacional.ValidacionesStratusConsulta();
				this.pageConsultatxInternacional.getDriver().switchTo().defaultContent();

				// Prueba informe----------------------------------------------------

				if (informe.equals("SI")) {
					Reporter.reportEvent(Reporter.MIC_PASS, "");
					// -----------------------------------------------------------------------------------------------------------------------
					/*
					 * Datos Fijos Middle Login, estos datos se encuentran el archivo
					 * data.properties
					 */
					// numCli tipoDoc numDoc clave tipoTok datoTok
					// Organiza los datos del cliente Middle con un array
					// numCli tipoDoc numDoc clave tipoTok datoTok

					DatosEmpresarial.loadLoginDataFija("0", numCliEmp, tipoDoc, numDoc, clave, datoTok);

					// Organiza los datos del cliente Middle con un array
					String[] datosLogin = DatosEmpresarial.getLoginData();
					// Reporta los datos del logeo
					Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
					// -----------------------------------------------------------------------------------------------------------------------

					// INTENTA HACER EL LOGUEO

					this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la
																		// prueba
					String msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal Middle
																	// Pyme.
					this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en
																// el
																// Ambiente Middle o FRONT
					// -----------------------------------------------------------------------------------------------------------------------

					// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
					if (msgError == null) {

						this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
						// -----------------------------------------------------------------------------------------------------------------------
						// NUMEROS DE FIRMAS A RALIZAR
						this.numAprobaciones = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();

						controllerValiPymeMiddle.ValidacionInformeTransInternacional();
					}
// -----------------------------------------------------------------------------------------------------------------------

					// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
//					this.pageLogin.CerrarSesionMiddle();
//					this.pageOrigen.closeAllBrowsers();
//					SettingsRun.exitTestIteration();

				} else {
					this.pageOrigen.getDriver().switchTo().defaultContent();
//					this.pageOrigen.click(cerrarSesion);
					internacionales = true;
				}
			} else if ((this.servicio.contains("Internacionales") || this.servicio.contains("Divisas"))
					&& this.servicio.contains("Aprobación") && !this.servicio.contains("Validar Estado")) {
				this.pageConsultatxInternacional = new PageConsultatxInternacional(this.pageLogin);
				this.pageConsultatxInternacional.ConsultaNumtx(this.servicio);
			}
			if (estado != null) {
				this.controller.SetEstado(estado);
			}

			if (DatosDavivienda.IS_RISKMOTOR) {

				if (this.servicio.contains("Nómina") || this.servicio.contains("Pago a Proveedores")
						|| this.servicio.contains("AFC") || this.servicio.contains("Crédito.3ros")) {
					this.controller.validarIngresoMRDestinosMasivo();
				} else {

					this.controller.validarIngresoMR();

				}
			}

			// Genera resultados sobre descuetos por transacci�n dentro, sobre y fuera del
			// rango
			if (cobros.equals("SI")) {
				this.pageAdminCombosCobros = new PageAdminCombosCobros(this.pageLogin);
				validacionDescuento = this.controller.validacionDescuentoTx(resultsMiddle, segmentosStratus,
						estadotxFinal, PageAdminCombosCobros.getInformacionConsultada());
			}

//			if (internacionales == false) {
//
//				this.pageLogin.CerrarSesionFront();
//			}

			double sumaCobros = 0;

			if (cobros.equals("SI")) {
				// realiza validaciones
				String tipoCobro = SettingsRun.getTestData().getParameter("Tipo Prueba Cobros");
				String verificarCombo = resultsMiddle.get(0).get("Verificacion Combo");
				if (tipoCobro.equals("COBROS CON COMBOS") && verificarCombo.contains("Empresa tiene combo")
						|| tipoCobro.equals("COBROS SIN COMBOS") && verificarCombo.contains("Empresa tiene combo")) {
					int totalCantidadTx = Integer.parseInt(resultsMiddle.get(0).get("Cantidad Tx Acumuladas"));
					if (totalCantidadTx > 0) {
						/*
						 * Datos Fijos Middle Login, estos datos se encuentran el archivo
						 * data.properties
						 */
						// numCli tipoDoc numDoc clave tipoTok datoTok
						DatosEmpresarial.loadLoginDataFija("0", SettingsRun.getGlobalData("MIDDLE.tipoDoc"),
								SettingsRun.getGlobalData("MIDDLE.numeroDeId"),
								SettingsRun.getGlobalData("MIDDLE.clavePersonal"),
								SettingsRun.getGlobalData("MIDDLE.tipoToken"),
								SettingsRun.getGlobalData("MIDDLE.numeroToken"));

						// -----------------------------------------------------------------------------------------------------------------------

						// Organiza los datos del cliente Middle con un array
						String[] datosLogin = DatosEmpresarial.getLoginData();
						Reporter.reportEvent(Reporter.MIC_INFO,
								"*** Datos de Logueo Middle: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
						// -----------------------------------------------------------------------------------------------------------------------
						// INTENTA HACER EL LOGUEO
						this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la
																			// prueba
						String msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal
																		// Middle
						// Pyme.
						this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta
																	// en
																	// el
																	// Ambiente Middle o FRONT
						this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
						this.controllerMiddleCobros = new ControllerMiddleCobros(this.pageLogin);

						int[] informacionCondatorPorTx = ControllerCrearTx.getTotalContador();
						this.controllerMiddleCobros.contadorFinal(informacionCondatorPorTx,
								resultsMiddle.get(0).get("Verificacion Combo"));
						// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
						this.pageLogin.CerrarSesionMiddle();
						Reporter.reportEvent(Reporter.MIC_INFO, "*** Cobros realizados en esta transacción");
						for (int i = 0; i < validacionDescuento.length; i++) {
							double valorCobro = validacionDescuento[i];
							Reporter.reportEvent(Reporter.MIC_INFO, "Valor de cobro " + (i + 1) + ": " + valorCobro);
							sumaCobros += validacionDescuento[i];
						}
						Reporter.reportEvent(Reporter.MIC_INFO, "*** Total cobros realizados: " + sumaCobros);
					} else if (totalCantidadTx <= 0) {
						Reporter.reportEvent(Reporter.MIC_INFO,
								"No se ha aplicado ninguna sumatoria de tx porque no se aplico ningun descuento");
					}
				} else {
					Reporter.reportEvent(Reporter.MIC_INFO,
							"*** Cobros realizados en esta transacción - Los cobros sin combos no aplica contador");
					for (int i = 0; i < validacionDescuento.length; i++) {
						double valorCobro = validacionDescuento[i];
						Reporter.reportEvent(Reporter.MIC_INFO, "Valor de cobro " + (i + 1) + ": " + valorCobro);
						sumaCobros += validacionDescuento[i];
					}
					Reporter.reportEvent(Reporter.MIC_INFO, "*** Total cobros realizados: " + sumaCobros);
					SettingsRun.exitTestIteration();
				}
			}
// -----------------------------------------------------------------------------------------------------------------------
			// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
			this.pageLogin.changeToDefaultFrame();
			this.pageLogin.CerrarSesionFront();
			SettingsRun.exitTestIteration();
		}
	}

// =======================================================================================================================

// ===========================================ADICIONAR A MOTOR DE RIESGO===========================================================================

	/**
	 * Realiza el registro en el set de Motor de Riesgo.<br>
	 * Los datos temporales ya deben existir: Time / Monto / NumeroTx / Resultado /
	 * Observacion
	 */
	public void adicionarRegistroMR(String tx) throws Exception {

		DatosDavivienda.RISKMOTOR.adicionarRegistro();
		// SETEA LOS REGISTROS QUE SON GUARDADOS DE MANERA TEMPORAL
//		if (tx.equals("Login")) {
		DatosDavivienda.RISKMOTOR.setTemporalTime(this.fechaHoraLogMR);
		DatosDavivienda.RISKMOTOR.setTime();
//		}
		DatosDavivienda.RISKMOTOR.setCanal("WEB_PYME");
		DatosDavivienda.RISKMOTOR.setAmbienteDePruebas(this.nombreAmbiente);
		
		DatosDavivienda.RISKMOTOR.setMonto();
		DatosDavivienda.RISKMOTOR.setNumeroTx();
		DatosDavivienda.RISKMOTOR.setResultado();
		DatosDavivienda.RISKMOTOR.setObservacion();
		String observa = SettingsRun.getTestData().getParameter("Desde_el_Detalle").trim();
		DatosDavivienda.RISKMOTOR.setObservacion(observa);

		String uuid = SettingsRun.getTestData().getParameter("Hash").trim();
		DatosDavivienda.RISKMOTOR.setHash(uuid);
		// Obtener el User-Agent

//		DatosDavivienda.RISKMOTOR.setUserAgent(this.userAgent);

//-----------------------------------------------------------------------------------------------------------------------

		if (this.controller == null && !this.tipoPrueba.equals("Login"))
			this.controller = new ControllerCrearTx(this.pageLogin);

		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();

		if (!tipoPrueba.equals(TP_LOGIN)) {
			DatosDavivienda.RISKMOTOR.setTransaccion(this.controller.getTransaccion(this.tipoPrueba, this.servicio));
		} else {
			DatosDavivienda.RISKMOTOR.setTransaccion("Login Exitoso");
		}

		String userType = "Token"; // VALOR POR DEFECTO
		if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
			userType = "Virtual";

		String intentos = SettingsRun.getTestData().getParameter("Ingresos Fallidos").trim();
		int intentosFall = 0; // VALOR POR DEFECTO
		if (DXCUtil.isInteger(intentos))
			intentosFall = Integer.valueOf(intentos);
//-----------------------------------------------------------------------------------------------------------------------
		// SETEA LOS DATOS FALTANTES
		DatosDavivienda.RISKMOTOR.setUserType(userType);
		DatosDavivienda.RISKMOTOR.setRisk(riesgoBc);
		if (!this.servicio.equals("Tx Internacionales Recibir desde el exterior")
				&& !this.servicio.equals("Tx Internacionales Enviar al exterior"))
			DatosDavivienda.RISKMOTOR.setRiskEFM(riesgoEfm);

//		if (transaccion.contains("Transferencias mismo nit")) {
//			DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("SI");
//		} else {
//			DatosDavivienda.RISKMOTOR.setIndicadorMismoNit("NO");
//		}

		DatosDavivienda.RISKMOTOR.setNumRetosFallidos(intentosFall);
//		DatosDavivienda.RISKMOTOR.setDireccionIp(this.ipPublica);
		DatosDavivienda.RISKMOTOR.setNavegador_SistemaOperativo(this.navegador);
		DatosDavivienda.RISKMOTOR.setCliente(DatosEmpresarial.CLI_EMPRESAR);
		DatosDavivienda.RISKMOTOR.setDocumento(DatosEmpresarial.TIPO_ID_LOGUEO, DatosEmpresarial.NUM_ID_LOGUEO);

		if (tx.equals("Login")) {
			DatosDavivienda.RISKMOTOR.setCuentaOrigen(Motorna);
			DatosDavivienda.RISKMOTOR.setCuentaDestino(Motorna);
			DatosDavivienda.RISKMOTOR.setNumCuentaOrigen(Motorna);
			DatosDavivienda.RISKMOTOR.setNumCuentaDestino(Motorna);
			DatosDavivienda.RISKMOTOR.set_CtaOrigenODestMIGRADA(Motorna);

			DatosDavivienda.RISKMOTOR.setNumTxCanal(Motorna);
//			DatosDavivienda.RISKMOTOR.setTipoIdEmpresa(Motorna);
			DatosDavivienda.RISKMOTOR.setNumIdEmpresa(Motorna);

		}

		// SETEA LOS REGISTROS PARA ERRORES EN LAS TRANSACCIONES DE TIPO LOGIN
		if (tx.equals("Error en Login")) {
			if (this.riesgo.equals("Bajo") || this.riesgo.equals("Medio"))
				DatosDavivienda.RISKMOTOR.setObservacion(
						"El comportamiento de la prueba no es correcto para el tipo de riesgo - error en login");

			DatosDavivienda.RISKMOTOR.setTemporalResultado("Declinada");
			DatosDavivienda.RISKMOTOR.setResultado();
		}

		Reporter.reportEvent(Reporter.MIC_PASS, "*** Se adiciona al MR");

	}
// =======================================================================================================================	

// ===========================================[Stratus]===========================================================================

	/**
	 * Realiza el Login de Stratus de Forma general
	 * 
	 * @throws Exception
	 */
	public void LoginStratus() throws Exception {
		if (stratus.equals("SI")) {

			// Obtiene los datos desde el archivo Dataproperties.
			String strusu = SettingsRun.getGlobalData("STRATUS.usuario");
			String strpass = SettingsRun.getGlobalData("STRATUS.password");

			// Se comentan las siguientes 3 lineas para que no ejecute en stratus
			if (DatosDavivienda.STRATUS == null) {
				DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
				Reporter.reportEvent(Reporter.MIC_INFO, "Se cargan datos para logueo" + " de Stratus");
			}
		}
	}

// ======================================================================================================================

	/**
	 * Metodo que llama y hace el logueo
	 * 
	 * @throws Exception
	 */
	public PageInicioC360 logueoC360() throws Exception {
		// INTENTA HACER EL LOGUEO - C360
		Reporter.write("*** Validar CLIENTE 360 ***");
		this.pageLoginC360 = new PageLogin(navegador);
		this.pageInicioC360 = new PageInicioC360(pageLoginC360);
		this.pageLoginC360.login360(SettingsRun.getGlobalData("data.c360User"),
				SettingsRun.getGlobalData("data.c360Pwd"));
		return this.pageInicioC360;
	}

}
