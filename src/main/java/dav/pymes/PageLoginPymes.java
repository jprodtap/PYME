package dav.pymes;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.DavUtil;
import dxc.dataSheets.DS_ProjectUrls;
import dxc.execution.BasePageWeb;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;
import launchTest.LaunchTestPyme;

public class PageLoginPymes extends BasePageWeb {

	private final static int NUM_MAX_INTENTO_LOGIN = 2;
	public final static String MSG_LOG_NO_ACCESO = "Acceso denegado";

	// ESTOS LOCATOR FUNCIONAN TANTO PARA FRONT COMO PARA MIDDLE
	String cmCliente = "//div[@id='divNumerClienteEmpresarial']";
//	By cmCliente = By.id("divNumerClienteEmpresarial");
	String cmTipoId = "//div[@id='divTipoIdentificacion']";
	String cmNumDocu = "//div[@id='divNumeroDocumento']";
	String cmClavePV = "//div[@id='divClavePersonal']"; // PUEDE SER CLAVE PERSONAL O VIRTUAL
	String cmNumTok = "//div[@id='divClaveToken']";

	By cmBtIngr = By.xpath("//input[@value='Ingresar']");
	By cmBtClosePop = By.xpath("//input[@value='Ingresar']");
//	By cmBtIngr = By.cssSelector("input[value=Ingresar]");
//	By cmBtClosePop = By.cssSelector("input[value=Ingresar]");
//	By cmBtAcept = By.cssSelector("a[class=boldcn][aria-label=Close]"); // BOTON ACEPTAR DE POPUPS
	By cmBtAcept = By.xpath("//a[@class='boldcn' and  @aria-label='Close']"); // BOTON ACEPTAR DE POPUPS

	String cmTexto = "//input[@type='text']"; // PARA CLIENTE EMPRESARIAL Y NUMERO DE DOCUMENTO
	String cmPassw = "//input[@type='password']"; // PARA CLAVE PERSONALoVIRTUAL Y NUMERO DE TOKEN
//	String cmPasswFront = "//input[@type='text']"; // PARA CLAVE PERSONALoVIRTUAL Y NUMERO DE TOKEN
	String cmSelect = "//select"; // PARA EL TIPO DE DOCUMENTO
	By cmCerrSes = By.xpath("//a[@id='CerrarSesion']"); // EN FRONT Y MIDDLE SE PRESENTA

	By fechaMas = By.id("lblMasterFecha"); // LOCALIZADOR UNICO PARA MIDDEL PYME

	By formu = By.id("form1"); // LOCALIZADOR UNICO PARA FRONT PYME LOGIN
	By seleEmpresa = By.xpath("//table[@class='tabla_top_bajo']//select[@id='dropMasterEmpresa']");

	By accesDenegado = By.xpath("//h1[@class='center']");
	By imNOtaRobot = By.xpath("//span[@id='recaptcha-anchor']");
	By btnRecaptcha = By.xpath("//button[@id='recaptcha-reload-button']");
	By btnResuelveeldesafio = By.xpath("//button[@id='solver-button']");
	By xcc = By.name("c-e682wtfewode");

	// SE USA PARA DETERMINAR LAS URLS A USAR EN LA PARTE WEB, SI SE REQUIERE:
	public static String URL_FRONT = "";
	public static String URL_MIDDLE = "";

	protected String portalCargado = "";

	Date fechaHoraLogMR = null;

	static String ambIngActual = null, ambIngresado = null;

	static String numCliMiddle = null, tipoDocMiddle = null, numDocMiddle = null, claveMiddle = null,
			tipoTokMiddle = null, datoTokMiddle = null;

//=======================================================================================================================
	public PageLoginPymes(String navegador) {
		super(navegador);
		try {
			this.cargarUrls();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
//***********************************************************************************************************************
	public PageLoginPymes(String navegador, String downloadFilePath) {
		super(navegador, downloadFilePath);
		try {
			this.cargarUrls();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//***********************************************************************************************************************
	public PageLoginPymes(BasePageWeb parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Metodo que carga las URLs de Front y Middle si no se han cargado.
	 */

	private void cargarUrls() throws Exception {
		if (DatosEmpresarial.AMBIENTE_TEST.equals(""))
			throw new Exception("PortalPyme ERROR -- No se ha cargado el ambiente de trabajo...");

		// CARGA LAS URLs SI NO SE HAN CARGADO:
		if (PageLoginPymes.URL_FRONT.equals("") || PageLoginPymes.URL_MIDDLE.equals("")) {
			DS_ProjectUrls datosUrl = new DS_ProjectUrls();
			PageLoginPymes.URL_FRONT = datosUrl.getUrl(DatosEmpresarial.AMBIENTE_TEST, "PYMES FRONT");
			PageLoginPymes.URL_MIDDLE = datosUrl.getUrl(DatosEmpresarial.AMBIENTE_TEST, "PYMES MIDDLE");
		}
	}

//***********************************************************************************************************************
	public String getPortalCargado() {
		return this.portalCargado;
	}

//***********************************************************************************************************************
	/**
	 * Metodo para hacer el logueo en el portal Front Pyme.<br>
	 * Retorna un String que indica si fue exitoso el logueo: cuando retorna [null]
	 * es porque fue exitoso, en caso contrario retorna un mensaje que indica lo que
	 * sucedio.<br>
	 * Requiere que se haya hecho la carga de los datos de login de
	 * [DatosEmpresarial].
	 */
	public String loginFront() throws Exception {
		this.portalCargado = DatosEmpresarial.PORTAL_FRONT_PYME;
		// true = PARA QUE HAGA REINTENTO EN CASO DE FALLA, VACIO INDICA QUE ES CON
		// CLAVE CORRECTA
		String msgError = this.loginFront("");
		if (msgError == null) { // EL LOGUEO SE HIZO
			DatosEmpresarial.ESTALOG_WEB = true;
			DatosEmpresarial.FECHAHORALOG_WEB = new Date();
			if (DatosDavivienda.IS_RISKMOTOR)
//				Evidence.save("HizoLogueo", this);
				Evidence.save("HizoLogueo");
		}
		return msgError;
	}

// ***********************************************************************************************************************
	/**
	 * Metodo para hacer el logueo en el portal Front Pyme.<br>
	 * Retorna un String que indica si fue exitoso el logueo: cuando retorna [null]
	 * es porque fue exitoso, en caso contrario retorna un mensaje que indica lo que
	 * sucedio.<br>
	 * Requiere que se haya hecho la carga de los datos de login de
	 * [DatosEmpresarial].
	 */
	public String loginFrontFirmas() throws Exception {
		this.portalCargado = DatosEmpresarial.PORTAL_FRONT_PYME;
		// true = PARA QUE HAGA REINTENTO EN CASO DE FALLA, VACIO INDICA QUE ES CON
		// CLAVE CORRECTA
		String msgError = this.loginFrontFirmas("");
		if (msgError == null) { // EL LOGUEO SE HIZO
			DatosEmpresarial.ESTALOG_WEB = true;
			DatosEmpresarial.FECHAHORALOG_WEB = new Date();
			if (DatosDavivienda.IS_RISKMOTOR)
//				Evidence.save("HizoLogueo", this);
				Evidence.save("HizoLogueo");
			Evidence.save("HizoLogueoh");
		}
		return msgError;
	}
//***********************************************************************************************************************
	/**
	 * @ZEA Metodo para hacer el logueo en el portal Front Pyme para Motor de
	 *      Riesgo.<br>
	 *      Retorna un String que indica si fue exitoso el logueo: cuando retorna
	 *      [null] es porque fue exitoso, en caso contrario retorna un mensaje que
	 *      indica lo que sucedio.<br>
	 *      Requiere que se haya hecho la carga de los datos de login de
	 *      [DatosEmpresarial].
	 */

	/*
	 * public String loginFrontMR(boolean conReintento, String condicion) throws
	 * Exception { if (DatosEmpresarial.ESTALOG_WEB) return null; // YA ESTA
	 * LOGUEADO, NO HACE NADA
	 * 
	 * this.portalCargado = DatosEmpresarial.PORTAL_FRONT_PYME; return
	 * this.login(conReintento, condicion); }
	 */
//***********************************************************************************************************************
	/**
	 * Metodo para hacer el logueo en el portal Middle Pyme.<br>
	 * Retorna un String que indica si fue exitoso el logueo: cuando retorna [null]
	 * es porque fue exitoso, en caso contrario retorna un mensaje que indica lo que
	 * sucedio.<br>
	 * Requiere que se haya hecho la carga de los datos de login de
	 * [DatosEmpresarial].
	 */
	public String loginMiddle() throws Exception {
		this.portalCargado = DatosEmpresarial.PORTAL_MIDDLE_PYME;
		String msgError = this.login(""); // VACIO INDICA QUE ES CON CLAVE CORRECTA
		if (msgError == null) { // EL LOGUEO SE HIZO
			DatosEmpresarial.ESTALOGMIDD_PYM = true;
			DatosEmpresarial.FECHAHORALOG_MIDD_PYM = new Date();
			this.getPortalCargado();
		}
		return msgError;
	}

// ***********************************************************************************************************************
	/**
	 * Metodo para hacer el logueo en el portal Middle Pyme.<br>
	 * Retorna un String que indica si fue exitoso el logueo: cuando retorna [null]
	 * es porque fue exitoso, en caso contrario retorna un mensaje que indica lo que
	 * sucedio.<br>
	 * Requiere que se haya hecho la carga de los datos de login de
	 * [DatosEmpresarial].
	 */
	public void loginMiddleActivartoken() throws Exception {
		this.portalCargado = DatosEmpresarial.PORTAL_MIDDLE_PYME;
		this.loginActivarToken(""); // VACiO INDICA QUE ES CON CLAVE CORRECTA
	}

//***********************************************************************************************************************
	/**
	 * Metodo que retorna el mensaje de alerta si este existe. Si el retorno es
	 * [null] es porque NO existe un mensaje de alerta.<br>
	 * Se recibe por parametro un listado de los posibles id de las alertas que se
	 * pueden presentar.
	 */
	public String getMsgAlertIfExist(String... idsAlerta) {
		String msgAlert = null;
		for (String id : idsAlerta) {
			By locMessage = By.id(id);
			if (this.isDisplayed(locMessage)) {
				msgAlert = this.getText(locMessage).trim();
				if (msgAlert.equals("")) // NO HAY MENSAJE
					msgAlert = null;
				else
					break; // PARA TERMINAR EL CICLO
			}
		}
		return msgAlert;
	}

//***********************************************************************************************************************
	/**
	 * Se encarga de darle click al elemento de CERRAR SESION.<br>
	 * <b>OJO:</b> El cierre de sesion garantiza el cierre del Browser.
	 */
	public void closeSession() {

		boolean estaLogueado = DatosEmpresarial.ESTALOG_WEB;
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
			estaLogueado = DatosEmpresarial.ESTALOGMIDD_PYM;
		if (!estaLogueado) {
			DXCUtil.wait(3);
			this.closeAllBrowsers(); // CIERRA EL BROWSER
			return; // NO ESTA LOGUEADO, NO HAY QUE CERRAR
		}
//-----------------------------------------------------------------------------------------------------------------------		

		if (this.element(cmCerrSes) != null) {
			this.click(cmCerrSes); // DA CLICK EN EL LOCATOR DE CERRAR SESION
		}
		DXCUtil.wait(6);
		boolean portalAbierto = false;
		do { // SE DEBE ESPERAR MIENTRAS EL PORTAL ESTA ABIERTO, SE SABE:
				// PARA EL CASO DE MIDDLE O EXPLORER - PORQUE EL CAMPO DE CERRAR SESION SE
				// PRESENTA
				// PARA EL CASO DE FRONT QUE NO ES EXPLORER - PORQUE EL BROWSER ESTA ABIERTO
			if (this.getNavegador().equals(BasePageWeb.EXPLORER)
					|| this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
				portalAbierto = this.isDisplayed(cmCerrSes);
		} while (portalAbierto);
//-----------------------------------------------------------------------------------------------------------------------		
		// INDICA QUE YA NO ESTA LOGUEADO Y GARANTIZA EL CIERRE DEL NAVEGADOR
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_FRONT_PYME)) {
			DatosEmpresarial.ESTALOG_WEB = false;
			if (this.getNavegador().equals(BasePageWeb.EXPLORER))
				DXCUtil.wait(3);
			if (this.ThereareOpenWindows())
			this.closeCurrentBrowser();
		} else {
			DatosEmpresarial.ESTALOGMIDD_PYM = false;
		}
	}

//***********************************************************************************************************************
	/**
	 * Primero revisa si el Popup de ACTIVE SUS CUENTAS se estapresentando, si es
	 * asi, lo cierra.
	 */
	public void closePopupActiveCtas() {
		WebElement objPopopActiveCtas = this.element(By.id("cphCuerpo_LikActivar"));
		if (objPopopActiveCtas != null)
			this.click(By.cssSelector("button[class=close]"));
	}

//***********************************************************************************************************************
	/**
	 * Este metodo termina la iteracion actual que se esta realizando en
	 * PortalPyme.<br>
	 * Si es tiempo de cerrar sesion, la cierra.<br>
	 * <b>OJO:</b> El cierre de sesion garantiza el cierre del Browser.
	 */
	public void terminarIteracion() throws Exception {
		if (this.isTimeCloseSession()) {
			this.closeSession();
		} else {
			this.closeSession();
		}
		SettingsRun.exitTestIteration();
	}

//***********************************************************************************************************************

	/**
	 * Solicita el flujo de prueba si es con contratacion o Confiar en la
	 * contratacion
	 */
	public static String seleccion() {
		String[] sele = { "SI", "NO", "Solo Contratar servicios", "Informe" };

		int posAmbiente;
		do {
			posAmbiente = JOptionPane.showOptionDialog(null, "Deseas volver a realizar la contratación ", null,
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sele, sele[0]);
		} while (posAmbiente < 0); // Es -1 si cierra el Dialog
		return sele[posAmbiente];
	}

	public static String SelecMR() {
		String[] sele = { "SI", "NO" };

		int posAmbiente;
		do {
			posAmbiente = JOptionPane.showOptionDialog(null, "Realizar el flujo para Motor de riesgo?", null,
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sele, sele[0]);
		} while (posAmbiente < 0); // Es -1 si cierra el Dialog
		return sele[posAmbiente];
	}

	/**
	 * Metodo selecionambienteClose: Marca si esta en el Ambiente Middle o FRONT
	 * 
	 * @param sel
	 * @return
	 */
	public static String selecionambienteClose(String sel) {

		String dato = null;

		if (sel == "SI") {
			ambIngresado = sel;
		}

		if (sel == "NO") {
			ambIngresado = sel;
		}
		return dato;
	}

	/**
	 * Indica si es tiempo de cerrar sesion en el PYMES.<br>
	 * Es tiempo de cerrar sesion, si se cumple alguna de las siguientes
	 * condiciones:<br>
	 * 1. El tiempo de logueo permitido ha expirado.<br>
	 * 2. Es la prueba final.<br>
	 * 3. El usuario a loguear en la siguiente prueba es diferente al actual.<br>
	 * 4. Para Front : El navegador a usar en la siguiente prueba es diferente al
	 * actual.
	 */
	public boolean isTimeCloseSession() throws Exception {
		boolean isTime = false;

//		boolean isFront = (this.getPortalCargado().equals(DatosEmpresarial.PORTAL_FRONT_PYME));

		ambIngActual = ambIngresado;
		String canal = DatosDavivienda.CANAL_PYME_MIDDLE;
		DXCUtil.wait(8);
		if (ambIngActual == "NO")
			canal = DatosDavivienda.CANAL_PYME_FRONT;

		if (DavUtil.tiempoActividadExpirado(canal))
			isTime = true; // CONDICION 1

		else if (SettingsRun.esIteracionFinal())
			isTime = true; // CONDICION 2

		else if (!DatosEmpresarial.currentAndNextCustomerIsEqual())
			isTime = true; // CONDICION 3

		else if (ambIngActual == "NO") {
			int nextRow = SettingsRun.getNextIteration();
			try { // SE PUEDE GENERAR EXCEPCION POR NO TENER EL PARAMETRO "Navegador" EN LA HOJA
					// DE DATOS
				String currentNavegador = SettingsRun.getTestData().getParameter("Navegador").trim();
				String nextNavegador = SettingsRun.getTestData().getParameterByExec("Navegador", nextRow).trim();
				isTime = !currentNavegador.equals(nextNavegador); // CONDICION 4
			} catch (Exception e) {
				// HACE NADA DEJA EL VALOR DE [isTime]
			}
		}
		return isTime;
	}

// =======================================================================================================================
	/**
	 * Metodo para hacer el logueo en el portal Front Pyme o Middle Pyme, depende de
	 * lo indicado por [this.portalCargado] Requiere que se haya hecho la carga de
	 * los datos de login de [DatosEmpresarial].<br>
	 * Retorna [null] si el logueo es exitoso, en caso contrario el respectivo
	 * mensaje de error.
	 * 
	 * @param condicion - Condicion del logueo a realizar.
	 */
	private String loginFront(String condicion) throws Exception {

		// {0-ClienteEmpresarial, 1-tipoId, 2-numId, 3-clave, 4-tipoToken}
		String[] datosLogin = DatosEmpresarial.getLoginData();
		String numCliEmp = datosLogin[0];
		String tipoDoc = datosLogin[1];
		String numDoc = datosLogin[2];
		String clave = datosLogin[3];
		String tipoTok = datosLogin[4];
// -----------------------------------------------------------------------------------------------------------------------
		// DETERMINA LA URL A CARGAR:
		String urlCargar = PageLoginPymes.URL_FRONT;
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
			urlCargar = PageLoginPymes.URL_MIDDLE;
// -----------------------------------------------------------------------------------------------------------------------
		// EMPIEZA A NAVEGAR:
		this.maximizeBrowser();
		this.NavegadorFront(urlCargar);
// -----------------------------------------------------------------------------------------------------------------------
		String msgAlerta = null;

		int intento = 0; // PARA SABER CUANTOS INTENTOS PARA HACER EL INGRESO, SOLO SE PERMITIRA 2
							// INTENTOS
		this.SiExistVerificacionRobot();

		do {
			do { // INGRESO DE DATOS INICIALES: MIENTRAS SALGA MENSAJE QUE DIGA QUE FALTA SI LLEGA A ESTE PUNTO, PUDO REALIZAR EL INGRESO DE LOS DATOS INICIALES,
				msgAlerta = this.ingresarDatosInicialesFront(numCliEmp, tipoDoc, numDoc);
				this.SiExistVerificacionRobot();
			} while (msgAlerta != null && msgAlerta.toUpperCase().contains("INGRESE")
					&& msgAlerta.toUpperCase().contains("Por favor Ingrese los datos."));

			// SI HAY MENSAJE DE ALERTA EN EL INGRESO DE LOS DATOS INICIALES: SE DEBE
			// TERMINAR LA PRUEBA, PORQUE NO SE PUDO REALIZAR EL LOGUEO
			if (msgAlerta != null) {
				String msgError = "\nRevise la data, debe haber algo ERRADO.";
				Reporter.reportEvent(Reporter.MIC_FAIL, "*** Revise la data, debe haber algo ERRADO");
				if (DXCUtil.containsIgnoreCaseAndAccents(msgAlerta, "SU CLAVE VIRTUAL EMPRESARIAL HA EXPIRADO"))
					msgError = "\nRecuerde cambiar la data con la nueva clave y si tiene otros clientes, revise sus claves antes de reiniciar la Automatización.";
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"*** Recuerde cambiar la data con la nueva clave y si tiene otros clientes, revise sus claves antes de reiniciar la Automatización.");
				return (msgAlerta + msgError);
			}
// -----------------------------------------------------------------------------------------------------------------------
			// SI LLEGA A ESTE PUNTO, PUDO REALIZAR EL INGRESO DE LOS DATOS INICIALES,
			// INGRESA CLAVE Y TOKEN (si se requiere):
			msgAlerta = this.ingresarClaveTokenFornt(clave, tipoTok, condicion);

			if (msgAlerta != null) {
				this.navigate(urlCargar); // DEJA LA PANTALLA EN LA PANTALLA DE LOGUEO
				if (msgAlerta.contains("ya existe una sesión abierta"))
					msgAlerta = "1"; // PARA QUE INTENTE DE NUEVO
				if (msgAlerta.contains("Acceso denegado")) {

					if (LaunchTestPyme.realizarMR != null && LaunchTestPyme.realizarMR.equals("SI")) {
//						Evidence.save("Riesgo alto, declina el acceso", this);
						Evidence.save("Riesgo alto, declina el acceso");
						Reporter.reportEvent(Reporter.MIC_WARNING, "*** Riesgo alto, declina el acceso");

					} else {
//						Evidence.save("Acceso denegado", this);
						Evidence.save("Acceso denegado");
					}
					return msgAlerta;
				}
				intento++;
				Reporter.reportEvent(Reporter.MIC_INFO, "*** Ya existe una sesión abierta");

			} else {
				if (LaunchTestPyme.realizarMR != null && LaunchTestPyme.realizarMR.equals("SI")) {

					Reporter.reportEvent(Reporter.MIC_PASS, "*** Riesgo medio o bajo, acceso correcto");
				} else {
					Reporter.reportEvent(Reporter.MIC_PASS, "Acceso correcto");

				}
//				Evidence.save("Riesgo medio o bajo, acceso correcto", this);
				Evidence.save("Riesgo medio o bajo, acceso correcto");
			}

		} while (msgAlerta != null && intento < NUM_MAX_INTENTO_LOGIN); // CUANDO NO HAY ALERTA SE PUDO INGRESAR AL
// -----------------------------------------------------------------------------------------------------------------------
		return msgAlerta;
	}

// =======================================================================================================================
	/**
	 * Metodo para hacer el logueo en el portal Front Pyme o Middle Pyme, depende de
	 * lo indicado por [this.portalCargado] Requiere que se haya hecho la carga de
	 * los datos de login de [DatosEmpresarial].<br>
	 * Retorna [null] si el logueo es exitoso, en caso contrario el respectivo
	 * mensaje de error.
	 * 
	 * @param condicion - Condici�n del logueo a realizar.
	 */
	private String loginFrontFirmas(String condicion) throws Exception {
		// {0-ClienteEmpresarial, 1-tipoId, 2-numId, 3-clave, 4-tipoToken}
		String[] datosLogin = DatosEmpresarial.getLoginData();
		Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		String numCliEmp = datosLogin[0];
		String tipoDoc = datosLogin[1];
		String numDoc = datosLogin[2];
		String clave = datosLogin[3];
		String tipoTok = datosLogin[4];

		String msgAlerta = null;
		int intento = 0; // PARA SABER CUANTOS INTENTOS PARA HACER EL INGRESO, S�LO SE PERMITIRA 2
							// INTENTOS

		// DETERMINA LA URL A CARGAR:
		String urlCargar = PageLoginPymes.URL_FRONT;
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
			urlCargar = PageLoginPymes.URL_MIDDLE;

		// EMPIEZA A NAVEGAR:
		this.maximizeBrowser();
		this.NavegadorFront(urlCargar);

		do {
			do { // INGRESO DE DATOS INICIALES: MIENTRAS SALGA MENSAJE QUE DIGA QUE FALTA
					// INGRESAR ALGO
				msgAlerta = this.ingresarDatosInicialesFront(numCliEmp, tipoDoc, numDoc);
				// SI LLEGA A ESTE PUNTO, PUDO REALIZAR EL INGRESO DE LOS DATOS INICIALES,
			} while (msgAlerta != null && msgAlerta.toUpperCase().contains("INGRESE"));

			// SI HAY MENSAJE DE ALERTA EN EL INGRESO DE LOS DATOS INICIALES: SE DEBE
			// TERMINAR LA PRUEBA, PORQUE NO SE PUDO REALIZAR EL LOGUEO
			if (msgAlerta != null) {
				String msgError = "\nRevise la data, debe haber algo ERRADO.";
				Reporter.reportEvent(Reporter.MIC_FAIL, "Revise la data, debe haber algo ERRADO");
				if (DXCUtil.containsIgnoreCaseAndAccents(msgAlerta, "SU CLAVE VIRTUAL EMPRESARIAL HA EXPIRADO"))
					msgError = "\nRecuerde cambiar la data con la nueva clave y si tiene otros clientes, revise sus claves antes de reiniciar la Automatización.";
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"Recuerde cambiar la data con la nueva clave y si tiene otros clientes, revise sus claves antes de reiniciar la Automatización.");
				return (msgAlerta + msgError);
			}
			// -----------------------------------------------------------------------------------------------------------------------
			// SI LLEGA A ESTE PUNTO, PUDO REALIZAR EL INGRESO DE LOS DATOS INICIALES,
			// INGRESA CLAVE Y TOKEN (si se requiere):
			msgAlerta = this.ingresarClaveTokenFornt(clave, tipoTok, condicion);
			if (msgAlerta != null) {
				this.navigate(urlCargar); // DEJA LA PANTALLA EN LA PANTALLA DE LOGUEO
				if (msgAlerta.contains("ya existe una sesión abierta"))
					msgAlerta = "1"; // PARA QUE INTENTE DE NUEVO
				intento++;
				Reporter.reportEvent(Reporter.MIC_FAIL, "ya existe una sesión abierta");
			}
		} while (msgAlerta != null && intento < NUM_MAX_INTENTO_LOGIN); // CUANDO NO HAY ALERTA SE PUDO INGRESAR AL
																		// PORTAL
		// -----------------------------------------------------------------------------------------------------------------------
		return msgAlerta;
	}

	public String NavegadorFront(String url) {
		this.navigate(url);
		return url;
	}
//=======================================================================================================================

	public static String[] datosMidell(String numCliEmpM, String tipoDocM, String numDocM, String claveM,
			String tipoTokM, String datoTokM) {
		numCliMiddle = numCliEmpM;
		tipoDocMiddle = tipoDocM;
		numDocMiddle = numDocM;
		claveMiddle = claveM;
		tipoTokMiddle = tipoTokM;
		datoTokMiddle = datoTokM;

		return null;
	}

	/**
	 * Metodo para hacer el logueo en el portal Front Pyme o Middle Pyme, depende de
	 * lo indicado por [this.portalCargado] Requiere que se haya hecho la carga de
	 * los datos de login de [DatosEmpresarial].<br>
	 * Retorna [null] si el logueo es exitoso, en caso contrario el respectivo
	 * mensaje de error.
	 * 
	 * @param condicion - Condici�n del logueo a realizar.
	 */
	private String login(String condicion) throws Exception {

		String numCliEmp1 = numCliMiddle;
		String tipoDoc1 = tipoDocMiddle;
		String numDoc1 = numDocMiddle;
		String clave1 = claveMiddle;
		String tipoTok1 = tipoTokMiddle;
//-----------------------------------------------------------------------------------------------------------------------
		// DETERMINA LA URL A CARGAR:
		String urlCargar = PageLoginPymes.URL_FRONT;
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
			urlCargar = PageLoginPymes.URL_MIDDLE;
//-----------------------------------------------------------------------------------------------------------------------
		// EMPIEZA A NAVEGAR:
		this.maximizeBrowser();
		this.navigate(urlCargar);
//-----------------------------------------------------------------------------------------------------------------------		
		String msgAlerta = null;
		int intento = 0; // PARA SABER CUANTOS INTENTOS PARA HACER EL INGRESO, S�LO SE PERMITIRA 2
							// INTENTOS
		do {
			do { // INGRESO DE DATOS INICIALES: MIENTRAS SALGA MENSAJE QUE DIGA QUE FALTA
					// INGRESAR ALGO
				msgAlerta = this.ingresarDatosIniciales(numCliEmp1, tipoDoc1, numDoc1);
			} while (msgAlerta != null && msgAlerta.toUpperCase().contains("INGRESE"));
			// SI HAY MENSAJE DE ALERTA EN EL INGRESO DE LOS DATOS INICIALES: SE DEBE
			// TERMINAR LA PRUEBA, PORQUE NO SE
			// PUDO REALIZAR EL LOGUEO
			if (msgAlerta != null) {
				String msgError = "\nRevise la data, debe haber algo ERRADO.";
				if (DXCUtil.containsIgnoreCaseAndAccents(msgAlerta, "SU CLAVE VIRTUAL EMPRESARIAL HA EXPIRADO"))
					msgError = "\nRecuerde cambiar la data con la nueva clave y si tiene otros clientes, revise sus claves antes de reiniciar la Automatización.";
				return (msgAlerta + msgError);
			}
//-----------------------------------------------------------------------------------------------------------------------		
			// SI LLEGA A ESTE PUNTO, PUDO REALIZAR EL INGRESO DE LOS DATOS INICIALES,
			// INGRESA CLAVE Y TOKEN
			// (si se requiere):
			msgAlerta = this.ingresarClaveToken(clave1, tipoTok1, condicion);
			if (msgAlerta != null) {
				this.navigate(urlCargar); // DEJA LA PANTALLA EN LA PANTALLA DE LOGUEO

				if (msgAlerta.contains("ya existe una sesión abierta"))
					if (msgAlerta.contains("ya existe una sesión abierta"))
						if (msgAlerta.contains("ya existe una sesión abierta"))
							if (msgAlerta.contains("ya existe una sesión abierta"))
								if (msgAlerta.contains("ya existe una sesión abierta"))
									msgAlerta = "1"; // PARA QUE INTENTE DE NUEVO
				intento++;
			}
		} while (msgAlerta != null && intento < NUM_MAX_INTENTO_LOGIN); // CUANDO NO HAY ALERTA SE PUDO INGRESAR AL
																		// PORTAL
//-----------------------------------------------------------------------------------------------------------------------		
		return msgAlerta;
	}

	// ***********************************************************************************************************************

	/**
	 * Metodo para hacer el logueo en el portal Front Pyme o Middle Pyme, depende de
	 * lo indicado por [this.portalCargado] Requiere que se haya hecho la carga de
	 * los datos de login de [DatosEmpresarial].<br>
	 * Retorna [null] si el logueo es exitoso, en caso contrario el respectivo
	 * mensaje de error.
	 * 
	 * @param condicion - Condici�n del logueo a realizar.
	 */
	private void loginActivarToken(String condicion) throws Exception {
//-----------------------------------------------------------------------------------------------------------------------
		// DETERMINA LA URL A CARGAR:
		String urlCargar = PageLoginPymes.URL_FRONT;
		if (this.portalCargado.equals(DatosEmpresarial.PORTAL_MIDDLE_PYME))
			urlCargar = PageLoginPymes.URL_MIDDLE;
//-----------------------------------------------------------------------------------------------------------------------
		// EMPIEZA A NAVEGAR:
		this.maximizeBrowser();
		this.navigate(urlCargar);
//-----------------------------------------------------------------------------------------------------------------------		
	}

//***********************************************************************************************************************
	/**
	 * Retorna [null] si el ingreso de los datos iniciales fue exitoso, en caso
	 * contrario retorna el error presentado dejando evidencia de esto.
	 */
	private String ingresarDatosIniciales(String numCliEmp, String tipoDoc, String numDoc) throws Exception {
//		Evidence.save("Login", this);
		Evidence.save("Login");
		boolean logueoShown;
		DXCUtil.wait(6);
		DXCUtil.Movercursor();
		do { // ESPERA MIENTRAS NO SE MUESTRE EL CAMPO DEL TIPO DE DOCUMENTO
			logueoShown = this.isDisplayed(this.element(cmTipoId));
		} while (!logueoShown);
//-----------------------------------------------------------------------------------------------------------------------        
		boolean isFront = (this.portalCargado.equals(DatosEmpresarial.PORTAL_FRONT_PYME));
		// S�LO EN EL FRONT : INGRESA EL N�MERO DE CLIENTE EMPRESARIAL
		if (isFront) {
			this.write(this.element(cmCliente + cmTexto), numCliEmp);
		}
//-----------------------------------------------------------------------------------------------------------------------        
		// SELECCIONA EL TIPO DE IDENTIFICACI�N
		String msgError = this.selectListItem(this.element(cmTipoId + cmSelect), tipoDoc);
		if (!msgError.equals("")) {
			this.closeAllBrowsers();
			Reporter.writeErr("No se encuentra el tipo de documento: " + msgError, true);
		}
//-----------------------------------------------------------------------------------------------------------------------        
		// INGRESA EL N�MERO DE DOCUMENTO DE IDENTIFICACI�N
		this.write(this.element(cmNumDocu + cmTexto), numDoc);
//-----------------------------------------------------------------------------------------------------------------------        
		// S�LO EN FRONT Y SI NO SE HA DESPLEGADO EL CAMPO DE LA CLAVE : DA CLICK EN
		// INGRESAR
		String msgAlerta = null;
		if (isFront && !this.isDisplayed(this.element(cmClavePV))) {
			this.click(cmBtIngr);
			boolean cmClaveShown;
			do { // ESPERA MIENTRAS NO SE MUESTRE EL CAMPO DE LA CLAVE O UN MENSAJE DE ALERTA
				cmClaveShown = this.isDisplayed(this.element(cmClavePV));
				msgAlerta = this.getMsgAlertIfExist("LbMensaje", "mensajeModal");
			} while (!cmClaveShown && msgAlerta == null);

			if (msgAlerta != null) { // HAY MENSAJE DE ALERTA
//				Evidence.save("ErrorData", this);
				Evidence.save("ErrorData");
				if (this.isDisplayed(cmBtAcept))
					this.click(cmBtAcept); // CIERRA EL MENSAJE PRESENTADO SI ES UN POPUP
			} // else : SI NO HAY ALERTA, SE PRESENTE EL CAMPO PARA INGRESAR LA CLAVE
		}
		return msgAlerta;
	}

// ***********************************************************************************************************************
	/**
	 * Retorna [null] si el ingreso de los datos iniciales fue exitoso, en caso
	 * contrario retorna el error presentado dejando evidencia de esto.
	 */
	private String ingresarDatosInicialesFront(String numCliEmp, String tipoDoc, String numDoc) throws Exception {
//		Evidence.save("LoginFront", this);
		Evidence.save("LoginFront");
		String scriptReloadPage = "window.location.reload(true);";
		boolean logueoShown;
		int cotador = 1;
		do { // ESPERA MIENTRAS NO SE MUESTRE EL CAMPO DEL TIPO DE DOCUMENTO
//            logueoShown = this.isDisplayed(cmNumDocu);
			DXCUtil.wait(1);
			if (cotador > 9) {
				// Actualizar la pagina para cache
				this.getJse().executeScript(scriptReloadPage);
			}
		} while (!this.isDisplayed(this.element(cmNumDocu)));
//-----------------------------------------------------------------------------------------------------------------------        
		boolean isFront = (this.portalCargado.equals(DatosEmpresarial.PORTAL_FRONT_PYME));
		// S�LO EN EL FRONT : INGRESA EL NÚMERO DE CLIENTE EMPRESARIAL
		if (isFront) {
			this.getJse().executeScript(scriptReloadPage);
			DXCUtil.wait(1);
			do {
				DXCUtil.wait(1);
			} while (this.element(cmCliente) == null && !this.isDisplayed(this.element(cmCliente)));
			this.write(this.element(cmCliente + cmTexto), numCliEmp);

//-----------------------------------------------------------------------------------------------------------------------        
			// SELECCIONA EL TIPO DE IDENTIFICACIÓN
			String msgError = this.selectListItem(this.element(cmTipoId + cmSelect), tipoDoc);
			if (!msgError.equals("")) {
				this.closeAllBrowsers();
				Reporter.writeErr("No se encuentra el tipo de documento: " + msgError, true);
			}
//-----------------------------------------------------------------------------------------------------------------------        
			// INGRESA EL NUMERO DE DOCUMENTO DE IDENTIFICACION
			DXCUtil.wait(1);
			this.write(this.element(cmNumDocu + cmTexto), numDoc);
			DXCUtil.BonotesTecla("TAB");

		}
//-----------------------------------------------------------------------------------------------------------------------        
		// SOLO EN FRONT Y SI NO SE HA DESPLEGADO EL CAMPO DE LA CLAVE : DA CLICK EN
		// INGRESAR
		String msgAlerta = null;
		if (isFront && !this.isDisplayed(this.element(cmClavePV))) {
			boolean cmClaveShown;
			this.click(cmBtIngr);
//			Evidence.save("Datos Iniciales", this);
			Evidence.save("Datos Iniciales");
			do { // ESPERA MIENTRAS NO SE MUESTRE EL CAMPO DE LA CLAVE O UN MENSAJE DE ALERTA
				cmClaveShown = this.isDisplayed(this.element(cmClavePV));
				msgAlerta = this.getMsgAlertIfExist("LbMensaje", "mensajeModal");
			} while (!cmClaveShown && msgAlerta == null);

			if (msgAlerta != null) { // HAY MENSAJE DE ALERTA
//				Evidence.save("ErrorData", this);
				Evidence.save("ErrorData");
				if (this.isDisplayed(cmBtAcept))
					this.click(cmBtAcept); // CIERRA EL MENSAJE PRESENTADO SI ES UN POPUP
			} // else : SI NO HAY ALERTA, SE PRESENTE EL CAMPO PARA INGRESAR LA CLAVE
		}
		return msgAlerta;
	}

//***********************************************************************************************************************
	/**
	 * Retorna [null] si el ingreso de la clave y el token fue exitoso, en caso
	 * contrario retorna el error presentado dejando evidencia de esto.
	 */
	private String ingresarClaveToken(String clave, String tipoTok, String condicion) throws Exception {
		// INGRESA LA CLAVE PERSONAL O VIRTUAL
		this.write(this.element(cmClavePV + cmPassw), clave);
//		Evidence.save("Datos Iniciales", this);
		Evidence.save("Datos Iniciales");
//		this.findElements(By.xpath("//*[@type='password']")).get(0).sendKeys(clave);
		// SI NO ES OTP : SE INGRESA EL VALOR DEL TOKEN
		if (!tipoTok.equals(DatosEmpresarial.TOKEN_OTP)) {
			String valToken = DatosEmpresarial.getArrayToken(condicion)[0];
			this.findElements(By.xpath("//*[@type='password']")).get(1).sendKeys(valToken);
		}
//-----------------------------------------------------------------------------------------------------------------------
		// DA CLICK EN INGRESAR: AVECES SE PRESENTA ERROR PORQUE SE ABRE POPUP QUE
		// BLOQUEA EL BOTON
		try {
			this.moveToElement(cmBtIngr);
			this.fechaHoraLogMR = new Date(); // HORA DE LOGUEO PAR EL MR
			this.clickNoScrollIntoView(cmBtIngr);
		} catch (ElementClickInterceptedException e) {
			this.closePopupSnippetCliente();
			this.fechaHoraLogMR = new Date(); // HUBO ERROR, NUEVA HORA DE LOGUEO PAR EL MR
			this.click(cmBtIngr);

		}
//-----------------------------------------------------------------------------------------------------------------------
		String msgAlerta;
//		boolean entroPortal;
		String entroPortal;
		do { // ESPERA MIENTRAS NO MUESTRE EL CERRAR SESION O SE PRESENTE UN MENSAJE DE
				// ALERTA
			try {
//				entroPortal = this.accedioAlPortal();
				entroPortal = this.accedioAlPortal();
				msgAlerta = this.getMsgAlertIfExist("LbMensaje", "lblMasterAlerta", "mensajeModal");
			} catch (Exception e) { // GARANTIZA QUE INGRESE DE NUEVO AL CICLO
				entroPortal = null;
				msgAlerta = null;
			}
		} while (entroPortal == null && msgAlerta == null);
//-----------------------------------------------------------------------------------------------------------------------
		if (msgAlerta != null) { // HAY MENSAJE DE ALERTA : AVECES PUEDE QUE HAYA HECHO EL LOGUEO
			String nbEvidence = "ErrorLogueo";
			if (entroPortal != null)
				nbEvidence = "MsgPopup"; // NO ES ERROR DE LOGUEO
//			Evidence.save(nbEvidence, this); // GUARDA EVIDENCIA DE LA ALERTA PRESENTADA
			Evidence.save(nbEvidence); // GUARDA EVIDENCIA DE LA ALERTA PRESENTADA
			if (this.isDisplayed(cmBtAcept))
				this.click(cmBtAcept); // CIERRA EL MENSAJE PRESENTADO SI ES UN POPUP
		}
		if (entroPortal == null)
			msgAlerta = null; // SE HIZO EL LOGUEO

		return msgAlerta;
	}

// ***********************************************************************************************************************
	/**
	 * Retorna [null] si el ingreso de la clave y el token fue exitoso, en caso
	 * contrario retorna el error presentado dejando evidencia de esto.
	 */
	private String ingresarClaveTokenFornt(String clave, String tipoTok, String condicion) throws Exception {

		String scriptReloadPage = "window.location.reload(true);";
		int cotador = 1;
		do { // ESPERA MIENTRAS NO SE MUESTRE EL CAMPO DEL TIPO DE DOCUMENTO
//          logueoShown = this.isDisplayed(cmNumDocu);
			DXCUtil.wait(1);
			if (cotador > 9) {
				// Actualizar la pagina para cache
				this.getJse().executeScript(scriptReloadPage);
			}
		} while (!this.isDisplayed((this.element(cmClavePV))));

		// Actualizar la pagina para cache
		this.getJse().executeScript(scriptReloadPage);
		// INGRESA LA CLAVE PERSONAL O VIRTUAL
		do {
			DXCUtil.wait(1);
			this.existDialogAccept();
		} while (!this.isDisplayed((this.element(cmClavePV))));

		if (this.element(cmNumTok) != null)
			this.focus(this.element(cmNumTok));
		else
			this.focus(this.element(cmClavePV));

		DXCUtil.wait(10);
		this.write(this.element(cmClavePV + cmTexto + "[1]"), clave);
//		Evidence.save("Ingreso pass", this);
		Evidence.save("Ingreso pass");
		// SI NO ES OTP : SE INGRESA EL VALOR DEL TOKEN
		if (!tipoTok.equals(DatosEmpresarial.TOKEN_OTP)) {
			String valToken = DatosEmpresarial.getArrayToken(condicion)[0];
			this.write(this.element(cmNumTok + cmTexto + "[1]"), valToken);
		}
//-----------------------------------------------------------------------------------------------------------------------
		// DA CLICK EN INGRESAR: AVECES SE PRESENTA ERROR PORQUE SE ABRE POPUP QUE
		// BLOQUEA EL BOTON
		try {
			this.moveToElement(cmBtIngr);
			this.fechaHoraLogMR = new Date(); // HORA DE LOGUEO PAR EL MR
			this.clickNoScrollIntoView(cmBtIngr);
		} catch (ElementClickInterceptedException e) {
			this.closePopupSnippetCliente();
			this.fechaHoraLogMR = new Date(); // HUBO ERROR, NUEVA HORA DE LOGUEO PAR EL MR
			this.click(cmBtIngr);
		}
//-----------------------------------------------------------------------------------------------------------------------
		String msgAlerta = this.getText(By.id("LbMensaje"));
		String entroPortal;

		if (msgAlerta != null) {

			do { // ESPERA MIENTRAS NO MUESTRE EL CERRAR SESION O SE PRESENTE UN MENSAJE DE
					// ALERTA
				try {
					entroPortal = this.accedioAlPortal();
					msgAlerta = this.getMsgAlertIfExist("LbMensaje", "lblMasterAlerta", "mensajeModal");
				} catch (Exception e) { // GARANTIZA QUE INGRESE DE NUEVO AL CICLO
					entroPortal = null;
					msgAlerta = null;
				}
			} while (entroPortal == null && msgAlerta == null);
//-----------------------------------------------------------------------------------------------------------------------
			if (msgAlerta != null) { // HAY MENSAJE DE ALERTA : AVECES PUEDE QUE HAYA HECHO EL LOGUEO
				String nbEvidence = "ErrorLogueo";
				if (entroPortal != null)
					nbEvidence = "MsgPopup"; // NO ES ERROR DE LOGUEO
//				Evidence.save(nbEvidence, this); // GUARDA EVIDENCIA DE LA ALERTA PRESENTADA
				Evidence.save(nbEvidence); // GUARDA EVIDENCIA DE LA ALERTA PRESENTADA
				if (this.isDisplayed(cmBtAcept))
					this.click(cmBtAcept); // CIERRA EL MENSAJE PRESENTADO SI ES UN POPUP
			}
		}

		return msgAlerta;
	}

//=======================================================================================================================
	/**
	 * Primero revisa si el PopUp en el logueo que opaca la ventana se estA
	 * presentando, si es asI lo cierra. Se reconoce porque se esta presentando el
	 * elemento de volver la pagina opaca.
	 */
	private void closePopupSnippetCliente() {
		WebElement objOpacity = this.element(By.id("backgroundOpacity"));
		if (objOpacity != null)
			this.click(By.id("closeButton"));
	}

//***********************************************************************************************************************

	/**
	 * Retorna TRUE si se encuentra en la sesion del portal. Se reconoce porque esta
	 * el Elemento de CERRAR SESION. Se maneja en un metodo, ya que el logueo puede
	 * abrir ventanas emergentes.
	 */
	public String accedioAlPortal() {
		// OBTIENE LA LISTA DE VENTANAS
		List<String> idsWind = this.getIdWindows();
		String portalWind = idsWind.get(idsWind.size() - 1);// RECUPERA LA VENTANA ACTUAL CON LA QUE SE VA INTERATUAR
		this.changeWindow(portalWind);// VENTANA ACTUAL CON LA QUE SE VA INTERATUAR
		return portalWind;
	}

// ***********************************************************************************************************************

	/**
	 * Metodo CerrarSesionMiddle: Cierra la sesion en la actual que se cuentra y
	 * procede al cierre del Browser.
	 */
	public void CerrarSesionMiddle() {
		if (this.isDisplayed(fechaMas))
			this.click(cmCerrSes);
		DXCUtil.wait(5);
//		if (this.ThereareOpenWindows())
		this.closeCurrentBrowser();
	}

// ***********************************************************************************************************************

	/**
	 * Metodo CerrarSesionFront: Cierra la sesion en la actual que se en cuentra.
	 */
	public void CerrarSesionFront() {
		if (this.isDisplayed(fechaMas))
			this.click(cmCerrSes);
		DXCUtil.wait(5);
		if (this.ThereareOpenWindows())
		this.closeCurrentBrowser();
	}

// ***********************************************************************************************************************
	/**
	 * Retorna la fecha y hora que deberia ser registrada para Motor de Riesgo.<br>
	 * Este dato se toma en 2 puntos diferentes dependiendo del escenario:<br>
	 * - Si no hay logueo, en el punto exacto en que se da click al "Ingresar".<br>
	 * - Si hay logueo cuando ingresa al portal Pymes.<br>
	 */
	public Date getFechaHoraLogMR() {
		return this.fechaHoraLogMR;
	}

	public void SiExistVerificacionRobot() {
		DXCUtil.wait(7);
		if (this.element(By.id("main-iframe")) != null) {

			WebElement iframe = this.findElement(By.id("main-iframe"));
			this.getDriver().switchTo().frame(iframe);
			if (this.element(accesDenegado) != null) {
				DXCUtil.wait(5);
				WebElement iframe2 = this
						.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div/div/div/div/iframe"));
				this.getDriver().switchTo().frame(iframe2);
				if (this.element(imNOtaRobot) != null) {
					this.click(imNOtaRobot);
					this.changeToDefaultFrame();
					this.getDriver().switchTo().frame(iframe);
					DXCUtil.wait(6);
					WebElement iframe3 = this.findElement(By.xpath("/html/body/div[3]/div[4]/iframe"));
					this.getDriver().switchTo().frame(iframe3);
					String scrip = "document.querySelector('#recaptcha-reload-button').click();";
					this.getJse().executeScript(scrip);
					DXCUtil.wait(3);
					this.mouseOver(this.element("//div[@class=\"button-holder help-button-holder\"]"));
					this.mouseClick();
				}
			}
		}
	}
}