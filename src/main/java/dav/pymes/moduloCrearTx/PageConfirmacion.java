package dav.pymes.moduloCrearTx;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import dav.pymes.PagePortalPymes;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.NotificacionSMS;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageConfirmacion extends PagePortalPymes {

	public final static String MSG_EXITO_GUARD = "Esta transacción"; // INDICA QUE FALTA FIRMA
	public final static String MSG_EXITO_ENV_OTP = "Para confirmar la transacción"; // INDICA QUE ENVIÓ OTP
	public final static String MSG_EXITO_APROB = "Usted aprobó"; // APROBACIÓN DESDE CAJA
	public final static String MSG_EXITO_DOC_Y_FOR_INTER = "Documentación requerida";
	public final static String MSG_EXITO_DOC_Y_FOR_COMPLETE_INF_INTER = "Complete su transacción hoy";
	public final static String MSG_EXITO_APROB_INTER = "Transacción recibida";
	public final static String MSG_EXITO_APROB_INTER2 = "Puede consultar el estado";
	public final static String MSG_EXITO_PAGO = "Su transacción ha sido recibida por el Portal"; // 1FIRMA / APROBACIÓN
																									// DESDE DETALLE
	public final static String MSG_EXITO_DECL1 = "Transacción declinada por seguridad"; // DECLINACIÓN 1FIRMA:APROBACIÓN
																						// DESDE DETALLE
	public final static String MSG_EXITO_DECL2 = "Sus transacciones han sido declinadas"; // DECLINACIÓN : APROBACIÓN
																							// DESDE CAJA
	public final static String MSG_EXITO_DECL3 = "Transacción rechazada.";
	public final static String MSG_EXITO_DECL4 = "Esta transacción NO";
	public final static String MSG_EXITO_DECL5 = "Transacción declinada";
	
	public final static String MSG_EXITO_DOCYFOR = "Documentación requerida";
	// DESDE CAJA
	public static final String MSG_ERROR_NO_OTP = "NO SE RECIBIÓ LA OTP, SE TERMINA LA PRUEBA";

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Confirmar']");
	By locBtGuardar = By.xpath("//input[@id='cphCuerpo_btnGuardarSinAprobar']");
	By locBtGuardarInternacional = By.xpath("//button[@id='btnGuardar']");
	By locBtAprobar = By.xpath("//input[@id='cphCuerpo_btnAprobarTransaccion']");
	By locCmTokenOtp = By.xpath("//input[@id='cphCuerpo_tbxTokenOtp' or @id='Token']");
	By locCmTokenOtpModif = By.xpath("//input[@id='cphCuerpo_tbxTokenOtpModif']");
	By locCmTokenOtpElim = By.xpath("//*[@id=\"cphCuerpo_tbxTokenOtpElim\"]");
	By locCmTokenOtpInternacional = By.id("Token");
	By locCmTokenOtpPen = By.xpath("//input[@id='cphCuerpo_tbxTokenOtpApEl']");

	By locCmCvv = By.xpath("//div[@id='cphCuerpo_divC']/input");
	By locCmCvvPenAp = By.xpath("//input[@id='cphCuerpo_tbxCodigoSeguridadApEl']");

	By locCmTokenOtpOrpa = By.xpath("//*[@id=\"formPincipal:claveToken\"]");
	By locCmMsgTokenOtpOrp = By.xpath("//*[@id=\"formPincipal\"]/table[3]/tbody/tr/td");

	By locCmTokenOtpActualizacionDatos = By.xpath("//*[@id=\"cphCuerpo_tbxTokenOtp\"]");
	By locCmMsgTokenActualizacionDatos = By.xpath("//*[@id=\"lblMasterAlerta\"]");

	By locCmTokenOtpADEF = By.xpath("//*[@id=\"app\"]/div/div/div[8]/div[3]/input[1]");
	By locCmTokenOtpADEFPend = By.xpath("//*[@id=\"app\"]/div/div/div[3]/div[3]/input[1]");

	By locNumDoc = By.xpath("//span[@id='cphCuerpo_lblNumeroDocumento' or @id='cphCuerpo_lb_numeroDocumento']");
	By locCostoTx = By.xpath(
			"//span[@id='cphCuerpo_lblcosto' or @id='cphCuerpo_lblInformacionDaviviendaCosto'or @id='cphCuerpo_lblCostoTransaccion' or @id='cphCuerpo_lblCosto']");
	By locLinkTxAprob = By.xpath("//span[@id='lblMasterAlerta']/a[@cssclass='LetraHypervinculoAzul']");

	By valorFinalTx = By.id("cphCuerpo_lblValorFinal");
	By valorFinalTx2 = By.id("cphCuerpo_lb_valor");
	By valorFinalTx3 = By.id("cphCuerpo_lblERMonto");
	By valorFinalTx4 = By.id("cphCuerpo_lblValor");
	By valorFinalTx5 = By.id("cphCuerpo_lblvalorpagar");
	By valorFinalTx6 = By.id("cphCuerpo_lb_TopeMaximo");
	By camValorTotaltxInter = By.xpath("//*[@id='FormPaso5']/div[2]/div[2]/div[9]/div[2]");

	By bancoDesTx = By.id("cphCuerpo_lbERCUBancoDestino");
	By bancoDesTx2 = By.id("cphCuerpo_lblDestinoBanco");
	By cambancoDesTxInter = By.xpath("//*[@id='FormPaso5']/div[4]/div[2]/div[3]/div[2]");

	By nbButtonAprTra = By.id("cphCuerpo_btnAprobarTransaccion");
	By nbButtonApro = By.id("cphCuerpo_btnAprobar");
	By nbButtonApro1 = By.id("cphCuerpo_btnAprobarTrx");
	By nbButtonApro2 = By.id("cphCuerpo_BtnAprobarModificacion");
	By nbButtonAprointernacional = By.xpath("//button[@class='dm-boton-proceso dm-btn-fondo-rojo'][1]");
	By popUpInternational = By.xpath("//button[@id='botonModal']");
	By locNumDocpInternational = By.xpath("//*[@id='AlertaModal']/div/div/div[2]/p/a");
	By btnPopup = By.id("btnmodal");
	By btnSeguroAprobar = By.xpath("//*[@id='botonModalAceptar']");
	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");
	By btnAprobarInscripcion = By.id("cphCuerpo_btnCrearInscripcionCuenta");
	By btnAprobarModificacion = By.id("cphCuerpo_btnDetalleModificar");
	By btnAprobarEliminacion = By.id("cphCuerpo_btnEliminarTransaccion");

	By table = By.xpath(
			"//*[@id=\"cphCuerpo_panConfirmacion\"]/table/tbody/tr[1]/td/table/tbody/tr[7]/td/table/tbody/tr[2]/td");
	By table2 = By.xpath("//*[@id=\"Pnl_cuerpo\"]/table/tbody/tr[2]/td/table/tbody/tr[9]/td");
//	private String costoTransaccion;
	private Date fechaHoraTx;

	public String nbButtonAprobarT = "Aprobar Transacción";
	public String nbButtonAprobar = "Aprobar";
	public String nbButtonAprobarIns = "Aprobar Inscripción";
	public String nbButtonAprobarMod = "Aprobar Modificación Inscripción";
	public String nbButtonAprobarElm = "Aprobar Eliminación Cuentas";
	public String nbButtonAprobarTxDT = "Aprobar Transacciones Seleccionadas";
	public String nbButtonAprobarT2 = "Aprobar Transacciones Seleccionadas";

//	String[] destinosMotor = null;

//=======================================================================================================================	
	public PageConfirmacion(BasePageWeb parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	public Date getFechaHoraTx() {
		return this.fechaHoraTx;
	}

	public void setNbButtonAprobar(String nbButtonAprobar) {
		this.nbButtonAprobar = nbButtonAprobar;
	}

//***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en "Confirmar". Da click en el botón "Guardar sin
	 * aprobar" y retorna el mensaje que se presenta.
	 */
	public String guardarSinAprobar() throws Exception {
		String msgRta = null;
		Evidence.save("GuardarSinAprobarH");
		Evidence.saveFullPage("GuardarSinAprobar", this);
		DXCUtil.wait(2);
		this.click(locBtGuardar);
		DXCUtil.wait(4);
		this.existDialogAccept();
		Evidence.saveFullPage("ConfirmaciónTx", this);
		DXCUtil.wait(5);
		msgRta =  this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgRta == null)
			msgRta = this.getActiveIntAlert();
		if (msgRta == null)
			msgRta = this.getMsgAlertIfExist("lblMasterTransaccionExitosaAlerta");
		
		return msgRta;
	}

//***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en "Confirmar". Da click en el botón "Guardar sin
	 * aprobar" y retorna el mensaje que se presenta.
	 */
	public String guardarSinAprobarInternacional() throws Exception {
		Evidence.save("GuardarSinAprobar");
		this.click(locBtGuardarInternacional);
		this.fechaHoraTx = new Date();
		return this.closeActiveIntAlert();
	}

//***********************************************************************************************************************
	public String aprobarTx() throws Exception {
		return aprobarTx(0, null, false);
	}

	public String aprobarTx(String valCodSeg, boolean desdeDetalle) throws Exception {
		return aprobarTx(0, valCodSeg, desdeDetalle);
	}

	public String aprobarTx(int numRetosFalla, boolean desdeDetalle) throws Exception {
		return aprobarTx(numRetosFalla, null, desdeDetalle);
	}

//***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en "Confirmar". Da click en "Aprobar Tansacción" si
	 * se presenta mesaje de alerta, se retorna dicho valor, si no, es porque se
	 * puede ingresar la dinámica, en tal caso se intenta hacerlo y con el código de
	 * seguridad (si se requiere), con el fin de "Confirmar Transacción".<br>
	 * Este método controla en número de restos de falla que se indiquen en
	 * [numRetosFalla].<br>
	 * Retorna el mensaje presentado y si número de documento se muestra, se
	 * almacena en [this.numeroDocumento]
	 */
	public String aprobarTx(int numRetosFalla, String valCodSeg, boolean desdeDetalle) throws Exception {

		String msgResp = null;

		boolean isCliVirtual = DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP);
		NotificacionSMS.loadHour(); // SIRVE PARA OTP
		String textnbTaprobar = null;
		DXCUtil.wait(4);
		this.existDialogAccept();
		DXCUtil.wait(4);

		if (this.element(nbButtonApro) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro);
		}
		if (this.element(nbButtonApro1) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro1);
		}
		if (this.element(nbButtonApro2) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro2);
		}

		if (this.element(nbButtonAprTra) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonAprTra);
		}

		if (this.element(nbButtonAprointernacional) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonAprointernacional);
		}
		if (this.element(btnAprobarInscripcion) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(this.btnAprobarInscripcion);
		}
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();

		if (tipoPrueba.equals("Inscribir cuenta") || tipoPrueba.equals("Inscripción por archivo")
				|| tipoPrueba.equals("Modificar cuenta") || tipoPrueba.equals("Eliminar cuenta")
				|| tipoPrueba.equals("Consultar inscripción") || tipoPrueba.equals("Autogestion de servicios")) {

			if (this.element(btnAprobarModificacion) != null) {
				DXCUtil.wait(1);
				textnbTaprobar = this.getText(this.btnAprobarModificacion);
			}

			if (this.element(btnAprobarEliminacion) != null) {
				DXCUtil.wait(1);
				textnbTaprobar = this.getText(this.btnAprobarEliminacion);
			}

		}

		// Aprobar inscripción
		if (textnbTaprobar.equals(this.nbButtonAprobarIns)) {
			DXCUtil.wait(2);
			this.click(this.btnAprobarInscripcion);
		}

		// Aprobar Modificación
		if (textnbTaprobar.equals(this.nbButtonAprobarMod)) {
			DXCUtil.wait(2);
			this.click(this.btnAprobarModificacion);
		}

		// Aprobar Eliminación
		if (textnbTaprobar.equals(this.nbButtonAprobarElm)) {
			DXCUtil.wait(2);
			this.click(this.btnAprobarEliminacion);
		}

		if (textnbTaprobar.equals(this.nbButtonAprobar)) {
			DXCUtil.wait(4);
			this.clickButton(this.nbButtonAprobar);
			DXCUtil.wait(5);
			this.existDialogAccept();
			if (this.element(By.id("lblMasterAlerta")) != null) {
				msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
				Evidence.saveAllScreens(msgResp, this);
			}

			if (msgResp != null) {

				if (!msgResp.contains(PageConfirmacion.MSG_EXITO_PAGO)
						&& !msgResp.contains(PageConfirmacion.MSG_EXITO_GUARD)
						&& !msgResp.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
						&& !msgResp.contains(PageConfirmacion.MSG_EXITO_APROB)) {
					return msgResp;
				}
			}
		}

		if (textnbTaprobar.equals(this.nbButtonAprobarT2)) {
			if (this.element(nbButtonAprointernacional) != null && this.isEnabled(nbButtonAprointernacional)) {
				DXCUtil.wait(4);
				this.click(nbButtonAprointernacional);
				DXCUtil.wait(4);
			} else {
				DXCUtil.wait(5);
				this.existDialogAccept();
				DXCUtil.wait(5);
				this.clickButton(this.nbButtonAprobarTxDT);
			}
		}

		if (textnbTaprobar.equals(this.nbButtonAprobarT)) {
			if (this.isDisplayed(nbButtonAprointernacional) && this.isEnabled(nbButtonAprointernacional)) {
				DXCUtil.wait(6);
				this.click(nbButtonAprointernacional);
				DXCUtil.wait(4);
			} else {
				DXCUtil.wait(5);
				this.clickButton(this.nbButtonAprobarT);
			}

		}

		if (this.isDisplayed(btnSeguroAprobar)) {
			this.click(btnSeguroAprobar);
		}

		WebElement objToken = null;
		boolean estaHabilitado = false;
		int contador = 1;

		DXCUtil.wait(3);
		if (this.element(nbButtonAprointernacional) == null) {
			do {
				DXCUtil.wait(5);
				msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
				
				if (msgResp == null)
					msgResp = this.getMsgAlertIfExist("AlertaModal");
				
				if (this.element(locCmTokenOtp) != null) {
					DXCUtil.wait(3);
					objToken = this.element(locCmTokenOtp);
				}
				// inscripcion--------
				if (this.element(locCmTokenOtpModif) != null) {
					DXCUtil.wait(3);
					objToken = this.element(locCmTokenOtpModif);
				}
				if (this.element(locCmTokenOtpElim) != null) {
					DXCUtil.wait(3);
					objToken = this.element(locCmTokenOtpElim);
				}
//				----------inscripcion
				if (this.element(locCmTokenOtpPen) != null) {
					DXCUtil.wait(3);
					objToken = this.element(locCmTokenOtpPen);
				}
				if (msgResp == null) 
				msgResp = this.getMsgAlertIfExist("mensaje");
				
				if (msgResp == null)
					msgResp = this.getMsgAlertIfExist("AlertaModal");
				
				if (msgResp!=null) {
				if (msgResp.contains("recibirá un código")) {
					if (this.element(By.xpath("//*[@id='botonModal']"))!=null) {
						this.click(By.xpath("//*[@id='botonModal']"));
					}
					
				}
				
				}
				if (objToken != null)
					estaHabilitado = this.isEnabled(objToken);
			} while (msgResp == null && !estaHabilitado && contador < 60);
		} else { // si es desde el modulo internacional
			do {
				DXCUtil.wait(6);

				if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP)) {
					msgResp = this.closeActiveIntAlert();
				}

				if (this.element(locCmTokenOtpInternacional) != null) {// campo otp comfirmacion Transferencias
																		// internacionales
					objToken = this.element(locCmTokenOtpInternacional);
				}
				String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
				String tipoPruebatx = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
				if ((servicio.equals("Tx Internacionales Enviar al exterior")
						|| servicio.equals("Tx Internacionales Recibir desde el exterior"))
						&& !tipoPruebatx.equals("Tx Pend Aprobación"))
					msgResp = this.closeActiveIntAlert();
				if (msgResp != null && msgResp.contains("no podemos atender su solicitud"))
					return msgResp;
				if (objToken != null)
					estaHabilitado = this.isEnabled(objToken);
				else if (objToken == null)
					return msgResp = " No se habilito el campo del código de confirmación ";

			} while (msgResp == null && estaHabilitado == false && contador < 60);
		}

//-----------------------------------------------------------------------------------------------------------------------
		if (estaHabilitado) { // SE PUEDE INGRESAR EL TOKEN / OTP
			if (isCliVirtual) {
				this.moveUpScreen();
				// Mod Para Interna
				Evidence.saveAllScreens("MsgEnvíoOtp", this);
			}
			DXCUtil.wait(1);
			msgResp = this.ingresarDinamica(numRetosFalla);
		} else if (!estaHabilitado && DatosDavivienda.IS_RISKMOTOR) {
			this.fechaHoraTx = new Date();
		} else if (!estaHabilitado) {
			return "No se habilito el campo del código de confirmación";
		}
//-----------------------------------------------------------------------------------------------------------------------
		// Mod Para Interna
		Evidence.save("AprobarTxh");
		Evidence.saveAllScreens("AprobarTx", this);

		return msgResp;
	}

//***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en "Confirmar". Da click en "Aprobar Tansacción" si
	 * se presenta mesaje de alerta, se retorna dicho valor, si no, es porque se
	 * puede ingresar la dinámica, en tal caso se intenta hacerlo y con el código de
	 * seguridad (si se requiere), con el fin de "Confirmar Transacción".<br>
	 * Este método controla en número de restos de falla que se indiquen en
	 * [numRetosFalla].<br>
	 * Retorna el mensaje presentado y si número de documento se muestra, se
	 * almacena en [this.numeroDocumento]
	 */
	public String aprobarTx2(int numRetosFalla) throws Exception {

		boolean isCliVirtual = DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP);
		String textnbTaprobar = null;
		this.existDialogAccept();
		DXCUtil.wait(4);

		if (this.element(nbButtonApro) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro);
		}
		if (this.element(nbButtonApro1) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro1);
		}

		if (this.element(nbButtonApro2) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonApro2);
		}

		if (this.element(nbButtonAprTra) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonAprTra);
		}

		if (this.element(nbButtonAprointernacional) != null) {
			DXCUtil.wait(1);
			textnbTaprobar = this.getText(nbButtonAprointernacional);
		}

		if (textnbTaprobar.equals(this.nbButtonAprobar)) {
			DXCUtil.wait(4);
			this.clickButton(this.nbButtonAprobar);
		}

		if (textnbTaprobar.equals(this.nbButtonAprobarT2)) {
			if (this.isDisplayed(nbButtonAprointernacional) && this.isEnabled(nbButtonAprointernacional)) {
				DXCUtil.wait(4);
				this.click(nbButtonAprointernacional);
				DXCUtil.wait(4);
			} else {
				DXCUtil.wait(5);
				this.existDialogAccept();
				DXCUtil.wait(5);
				this.clickButton(this.nbButtonAprobarTxDT);
			}
		}

		if (textnbTaprobar.equals(this.nbButtonAprobarT)) {
			if (this.isDisplayed(nbButtonAprointernacional) && this.isEnabled(nbButtonAprointernacional)) {
				DXCUtil.wait(6);
				this.click(nbButtonAprointernacional);
			} else {
				DXCUtil.wait(5);
				this.clickButton(this.nbButtonAprobarT);
			}
		}

		if (this.isDisplayed(btnSeguroAprobar)) {
			this.click(btnSeguroAprobar);
		}
		NotificacionSMS.loadHour(); // SIRVE PARA OTP
		String msgResp = null;
		WebElement objToken = null;
		boolean estaHabilitado = false;
		int contador = 1;
		if (this.element(nbButtonAprointernacional) == null) {
			do {
				DXCUtil.wait(1);
				msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
				if (this.element(locCmTokenOtp) != null) {
					objToken = this.element(locCmTokenOtp);
				}
				if (this.element(locCmTokenOtpPen) != null) {
					objToken = this.element(locCmTokenOtpPen);
				}
				if (objToken != null)
					estaHabilitado = this.isEnabled(objToken);
			} while (msgResp == null && !estaHabilitado && contador < 60);
		} else { // si es desde el modulo internacional
			do {
				DXCUtil.wait(6);

				if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP)) {
					msgResp = this.closeActiveIntAlert();
				}

				if (this.element(locCmTokenOtpInternacional) != null) {// campo otp comfirmacion Transferencias
					// internacionales
					objToken = this.element(locCmTokenOtpInternacional);
				}

				if (this.element(locCmTokenOtpInternacional) != null) { // campo otp aprobaciones desde el detalle
					objToken = this.element(locCmTokenOtpInternacional);
				}

				if (objToken != null)
					estaHabilitado = this.isEnabled(objToken);
				else if (objToken == null)
					return msgResp = " No se habilito el campo del código de confirmación ";

			} while (msgResp == null && estaHabilitado && contador < 60);
		}

//-----------------------------------------------------------------------------------------------------------------------
		if (estaHabilitado) { // SE PUEDE INGRESAR EL TOKEN / OTP
			if (isCliVirtual) {
				this.moveUpScreen();
				// Mod Para Interna
				Evidence.saveAllScreens("MsgEnvíoOtp", this);
			}
			DXCUtil.wait(1);
			msgResp = this.ingresarDinamica(numRetosFalla);
		} else if (!estaHabilitado && DatosDavivienda.IS_RISKMOTOR) {
			this.fechaHoraTx = new Date();
		} else if (!estaHabilitado) {
			return "No se habilito el campo del código de confirmación";
		}
//-----------------------------------------------------------------------------------------------------------------------
		// Mod Para Interna
		Evidence.save("AprobarTxh");
		Evidence.saveAllScreens("AprobarTx", this);

		return msgResp;
	}

	// ***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en ingresar OTP/Token. Ingresa OTP/Token le da al
	 * boton confirmar. aprueba la transaccion de Ordenes de Pago.
	 */

	public String aprobarTxOrdenDePago() throws Exception {

		boolean isCliVirtual = DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP);
		this.existDialogAccept();
		DXCUtil.wait(4);

		NotificacionSMS.loadHour(); // SIRVE PARA OTP
		String msgResp = null;
		WebElement objToken = null;
		boolean estaHabilitado = true;

		// -----------------------------------------------------------------------------------------------------------------------
		if (estaHabilitado) { // SE PUEDE INGRESAR EL TOKEN / OTP
			if (true) {
				this.moveUpScreen();
				// Mod Para Interna
				Evidence.saveAllScreens("MsgEnvíoOtp", this);
			}
			DXCUtil.wait(1);
			msgResp = this.ingresarDinamica(0);

		} else if (!estaHabilitado && DatosDavivienda.IS_RISKMOTOR) {
			this.fechaHoraTx = new Date();

			// -----------------------------------------------------------------------------------------------------------------------
			// Mod Para Interna
			Evidence.save("AprobarTxh");
			Evidence.saveAllScreens("AprobarTx", this);

		}

		if (msgResp.equals("")) {

			this.click(By.id("formPincipal:continuar"));

			DXCUtil.wait(3);

			WebElement msgOrpa = this.element(locCmMsgTokenOtpOrp);
			msgResp = msgOrpa.getText();

			Evidence.saveFullPage("Mensaje aprobacion", this);

			this.click(By.id("formPincipal:continuar"));
		}

		return msgResp;
	}

//***********************************************************************************************************************
	/**
	 * Retorna el número de documento que se presenta en la pantalla de
	 * Confirmación. En caso que no se presente el retorno es [null].
	 */
	public String getNumeroDocumento() {
		String numeroDocumento = null;
		this.fechaHoraTx = new Date(); // ESTA ES LA FECHA Y HORA DE LA TRANSACCIÓN, SI SE REALIZA
		WebElement elementDoc = this.element(locNumDoc);
		if (elementDoc != null)
			numeroDocumento = elementDoc.getText();
		return numeroDocumento;
	}

// ***********************************************************************************************************************
	/**
	 * Retorna el Valor que se presenta en la pantalla de Confirmación. En caso que
	 * no se presente el retorno es [null].
	 */
	public String getValorFinalTx() {
		String valorFinal = null;
		WebElement elementvalor = this.element(valorFinalTx);
		WebElement elementvalor2 = this.element(valorFinalTx2);
		WebElement elementvalor3 = this.element(valorFinalTx3);
		WebElement elementvalor4 = this.element(valorFinalTx4);
		WebElement elementvalor5 = this.element(valorFinalTx5);
		WebElement elementvalor6 = this.element(valorFinalTx6);
		WebElement elementvalor7 = this.element(camValorTotaltxInter);
		if (elementvalor != null)
			valorFinal = elementvalor.getText();
		if (elementvalor2 != null)
			valorFinal = elementvalor2.getText();
		if (elementvalor3 != null)
			valorFinal = elementvalor3.getText();
		if (elementvalor4 != null)
			valorFinal = elementvalor4.getText();
		if (elementvalor5 != null)
			valorFinal = elementvalor5.getText();
		if (elementvalor6 != null)
			valorFinal = elementvalor6.getText();
		if (elementvalor7 != null)
			valorFinal = this.element(elementvalor7).getText();
		return valorFinal;
	}

// ***********************************************************************************************************************
	/**
	 * Retorna el Valor que se presenta en la pantalla de Confirmación. En caso que
	 * no se presente el retorno es [null].
	 */
	public String getBancoDesTx() {
		String valorFinal = null;
		WebElement elementDoc = this.element(bancoDesTx);
		if (elementDoc != null)
			valorFinal = elementDoc.getText();
		WebElement elementDoc2 = this.element(bancoDesTx2);
		if (elementDoc2 != null)
			valorFinal = elementDoc2.getText();
		WebElement elementDoc3 = this.element(cambancoDesTxInter);
		if (elementDoc3 != null)
			valorFinal = this.element(elementDoc3).getText();
		return valorFinal;
	}

// ***********************************************************************************************************************
	/**
	 * Retorna el número de documento que se presenta en la pantalla de
	 * Confirmación. En caso que no se presente el retorno es [null].
	 * 
	 * @throws Exception
	 */
	public String getNumeroDocumentoInterna() throws Exception {
		String numeroDocumento = null;
		this.fechaHoraTx = new Date(); // ESTA ES LA FECHA Y HORA DE LA TRANSACCIÓN, SI SE REALIZA
		WebElement elementDoc = this.element(locNumDocpInternational);
		DXCUtil.wait(2);
		if (elementDoc != null)
			numeroDocumento = elementDoc.getText();
		if (elementDoc == null)
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se vizualiza el element del documento Tx");
		if (this.element(btnPopup) != null) {
			this.click(btnPopup);
		}
		return numeroDocumento;
	}

//***********************************************************************************************************************
	public String ingresarDinamica(int numRetosFalla) throws Exception {

		String dato = "Token";
		if (DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP))
			dato = "Otp";
//-----------------------------------------------------------------------------------------------------------------------
		// ALISTA EL ARRAY DE TOKEN / OTP
		String[] arrOtps = DatosEmpresarial.getArrayToken();
		if (arrOtps.length == 0)
			return MSG_ERROR_NO_OTP;

		String[] arrTokIngresar = new String[numRetosFalla + 1];
		if (numRetosFalla == 0)
			arrTokIngresar = arrOtps;
		else {
			int longTok = arrOtps[0].length();
			int lastNum = Integer.valueOf(DXCUtil.right(arrOtps[0], 1));
			for (int posArr = 0; posArr < numRetosFalla; posArr++) {
				lastNum++; // PARA QUE SEA UN NÚMERO DIFERENTE DEL ÚLTIMO DÍGITO DEL TOKEN / OTP
				arrTokIngresar[posArr] = DXCUtil.left(arrOtps[0], longTok - 1)
						+ DXCUtil.right(String.valueOf(lastNum), 1);
			}
			arrTokIngresar[numRetosFalla] = arrOtps[0]; // DE ÚLTIMAS SE DEJA EL CORRECTO
		}
//-----------------------------------------------------------------------------------------------------------------------
		WebElement objCvv = this.element(locCmCvv);

		boolean pideCvv = (objCvv != null);

		WebElement objCvvPen = this.element(locCmCvvPenAp);

		boolean objCvvPenAp = (objCvvPen != null);

		String msgRta = null;
		int numIngresos = 0;

		for (int posArr = 0; posArr < arrTokIngresar.length; posArr++) {
			// Ejecuta JavaScript para desactivar el script
			this.getJse().executeScript(
					"var script = document.querySelector('script[src*=\"CSNYP.js\"]'); if (script) { script.parentNode.removeChild(script); }");
			if (this.element(locCmTokenOtpADEFPend) != null) {
				if (!this.isEnabled(locCmTokenOtpADEFPend))
					break; // NO PUEDE INGRESAR DINÁMICA
				this.focus(locCmTokenOtpADEFPend);
				this.write(locCmTokenOtpADEFPend, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpADEF) != null) {
				if (!this.isEnabled(locCmTokenOtpADEF))
					break; // NO PUEDE INGRESAR DINÁMICA
				this.focus(locCmTokenOtpADEF);
				this.write(locCmTokenOtpADEF, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpOrpa) != null) {
				if (!this.isEnabled(locCmTokenOtpOrpa))
					break; // NO PUEDE INGRESAR DINÁMICA
				this.focus(locCmTokenOtpOrpa);
				this.write(locCmTokenOtpOrpa, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpInternacional) != null) {
				if (!this.isEnabled(locCmTokenOtpInternacional))
					break; // NO PUEDE INGRESAR DINÁMICA
				DXCUtil.wait(2);
//				String scriptReloadPage = "var elementInsideIframe = document.querySelector('#Token');if(elementInsideIframe){elementInsideIframe.focus();elementInsideIframe.scrollIntoView(true);window.top.focus();} else {console.log('Element #Token inside iframe not found.');}";
//				this.getJse().executeScript(scriptReloadPage);
				this.focus(locCmTokenOtpInternacional);
				DXCUtil.wait(3);
						msgRta = this.getActiveIntAlert();// sdd
						if (msgRta != null&& !msgRta.isEmpty()) {
							return msgRta;
						}
				
				if (this.isEnabled(locCmTokenOtpInternacional)) 
				this.write(locCmTokenOtpInternacional, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtp) != null) {

				if (!this.isEnabled(locCmTokenOtp))
					break; // NO PUEDE INGRESAR DINÁMICA
				this.focus(locCmTokenOtp);
				if (this.isEnabled(locCmTokenOtp)) 
				this.write(locCmTokenOtp, arrTokIngresar[posArr]);
				msgRta = "";
			}
			
			if (this.element(locCmTokenOtpPen) != null) {
				if (!this.isEnabled(locCmTokenOtpPen))
					break; // NO PUEDE INGRESAR DINÁMICA
				this.focus(locCmTokenOtpPen);
				this.write(locCmTokenOtpPen, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpPen) != null) {
				if (!this.isEnabled(locCmTokenOtpPen))
					break; // NO PUEDE INGRESAR DINAMICA
				this.focus(locCmTokenOtpPen);
				this.write(locCmTokenOtpPen, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpModif) != null) {
				if (!this.isEnabled(locCmTokenOtpModif))
					break; // NO PUEDE INGRESAR DINAMICA
				this.focus(locCmTokenOtpModif);
				this.write(locCmTokenOtpModif, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (this.element(locCmTokenOtpElim) != null) {
				if (!this.isEnabled(locCmTokenOtpElim))
					break; // NO PUEDE INGRESAR DINMICA
				this.focus(locCmTokenOtpElim);
				this.write(locCmTokenOtpElim, arrTokIngresar[posArr]);
				msgRta = "";
			}

			if (pideCvv) {
				String cvv = SettingsRun.getTestData().getParameter("CVV-(Código de seguridad TC)").trim();
				this.write(locCmCvv, cvv);

			} else if (objCvvPenAp) {
				String cvv = SettingsRun.getTestData().getParameter("CVV-(Código de seguridad TC)").trim();
				this.write(locCmCvvPenAp, cvv);
			}

			DXCUtil.wait(3);

			this.fechaHoraTx = new Date(); // ESTA ES LA FECHA Y HORA DE LA TRANSACCIÓN, SI SE REALIZA
			Evidence.save("Ingreso" + dato + "(" + (posArr + 1) + ")");
//			Evidence.save("Ingreso" + dato + "(" + (posArr + 1) + ")", this);
			int contadorConfirmar = 0;

			if (this.element(locCmTokenOtpInternacional) != null && this.isEnabled(locCmTokenOtpInternacional)) {
				DXCUtil.wait(3);
				DXCUtil.BonotesTecla("TAB");
				DXCUtil.BonotesTecla("ENTER");
				Evidence.save("Confirmación Clave");
//				Evidence.save("Confirmación Clave", this);
				DXCUtil.wait(5);
			}

			if (!this.isEnabled(locCmTokenOtpInternacional) && !this.isEnabled(locCmTokenOtpOrpa)
					&& !this.isEnabled(locCmTokenOtpADEF) && !this.isEnabled(locCmTokenOtpADEFPend)) {
				// *Mirara si molesta en los pagos o diferentes modulos
				// */------------------------------------------------------------

				String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();

				if (tipoPrueba.equals("Inscribir cuenta") || tipoPrueba.equals("Inscripción por archivo")
						|| tipoPrueba.equals("Modificar cuenta") || tipoPrueba.equals("Eliminar cuenta")
						|| tipoPrueba.equals("Consultar inscripción")
						|| tipoPrueba.equals("Autogestion de servicios")) {

					if (this.element(By.xpath("//input[@type='submit'][@value='Confirmar Transacción']")) != null) {

						this.element("//input[@type='submit'][@value='Confirmar Transacción']").click();
					} else if (this.element(By.xpath("//input[@type='submit'][@value='Confirmar Eliminación']")) != null
							|| this.element(By.xpath("//*[@id='cphCuerpo_btnConfEliminar']")) != null) {
						this.clickButton("Confirmar Eliminación");
					} else if (this
							.element(By.xpath("//input[@type='submit'][@value='Confirmar Modificación']")) != null) {
						this.clickButton("Confirmar Modificación");
					} else if (this.findElement(By.id("btnConfirmar")) != null
							&& this.isDisplayed(By.id("btnConfirmar")) != null) {
						this.clickButton("Confirmar Transacción");
					}
				} else {
					DXCUtil.wait(1);
					if (this.isEnabled(this.elementButton("Confirmar Transacción"))) {
						this.element("//*[@value='Confirmar Transacción']").click();
//						this.click(By.xpath("//*[@value='Confirmar Transacción']"));
					}
					if (this.isDisplayed(By.xpath("//*[@value='Confirmar']")))
//						this.click(By.xpath("//*[@value='Confirmar']"));
						this.element(By.xpath("//*[@value='Confirmar']")).click();

					if (this.isEnabled(this.elementButton("Actualizar Datos"))) {
						this.mouseOver(By.xpath("(//*[@id='cphCuerpo_TbAprobacion']//td)[6]"));
						this.mouseClick();
						DXCUtil.wait(5);
						DXCUtil.BonotesTecla("TAB");
						this.clickButton("Actualizar Datos");
					}

				}
			}
			// ------------------------------------------------------------------------------------------------------------------------
			int contador = 0;
			numIngresos++;
			if (!this.isEnabled(locCmTokenOtpInternacional) && !this.isEnabled(locCmTokenOtpOrpa)
					&& !this.isEnabled(locCmTokenOtpADEF) && !this.isEnabled(locCmTokenOtpADEFPend)) {
				int conteo = 0;
				do { // ESPERA RESPUESTA DEL INGRESO DEL TOKEN
					DXCUtil.wait(1);
					msgRta = this.getMsgAlertIfExist("lblMasterAlerta");
					if (msgRta == null)
						msgRta = this.getMsgAlertIfExist("AlertaModal");
					if (msgRta == null)
						msgRta = this.getActiveIntAlert();
					if (msgRta == null)
						msgRta = this.getMsgAlertIfExist("lblMasterTransaccionExitosaAlerta");

					conteo++;
				} while ((msgRta == null || msgRta.contains(MSG_EXITO_ENV_OTP)) && conteo < 10);

				if (msgRta == null) {
					Evidence.saveFullPage("Aler", this);
					Reporter.write("Error en la aprobacion");
					this.pageLogin.CerrarSesionFront();
					SettingsRun.exitTestIteration();
				}
				Evidence.save("Aprobacion de transaccion hr");
//				Evidence.saveFullPage("Aprobacion de transaccion", this);
			}

			String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
			if (!tipoPrueba.equals("Tx Pend Aprobación")) {
				DXCUtil.wait(4);
				if (this.isEnabled(locCmTokenOtpInternacional)) {
					do { // ESPERA RESPUESTA DEL INGRESO DEL TOKEN
						DXCUtil.wait(1);
						msgRta = this.getActiveIntAlert();// sdd
						contador++;
						if (contador >= 30) {
							return msgRta;
						}
					} while (msgRta == null || msgRta.contains(MSG_EXITO_ENV_OTP));
				}
			} else {
				if (this.isEnabled(locCmTokenOtpInternacional)) {

					do { // ESPERA RESPUESTA DEL INGRESO DEL TOKEN
						DXCUtil.wait(1);
						msgRta = this.getActiveIntAlert();// sdd
						if (msgRta == null) {
							msgRta = this.closeActiveIntAlert();
						}
						contador++;
						if (contador >= 30) {
							return msgRta;
						}
					} while (msgRta == null || msgRta.contains(MSG_EXITO_ENV_OTP));
				}
			}
		}

		msgRta = this.getMsgAlertIfExist("lblMasterAlerta");// sdd
		if (msgRta == null)
			msgRta = this.getMsgAlertIfExist("lblMasterTransaccionExitosaAlerta");
		if (msgRta == null)
			msgRta = this.getMsgAlertIfExist("AlertaModal");
		if (msgRta != null && msgRta.contains(
				"Su código de aprobación ha expirado. De clic nuevamente en Aprobar transacción para generar un código nuevo.")) {
//			DXCUtil.wait(60);
			msgRta = this.aprobarTx2(0);
		}
		if (numRetosFalla != 0 && numRetosFalla + 1 != numIngresos && !msgRta.contains(
				"Su código de aprobación ha expirado. De clic nuevamente en Aprobar transacción para generar un código nuevo.")) // PIDE
																																	// NEXT
																																	// TOKEN
			msgRta += "\nPERO NO REALIZÓ LOS RETOS SOLICITADOS : [" + numRetosFalla + "]";
		return msgRta;
	}

//***********************************************************************************************************************
	/**
	 * Este método espera a que se presente la pantalla confirmar, puede pasar que
	 * se presente un error en tal caso este mensaje es retornado. Evalúa que si se
	 * presenta el Popup de ACH se tome evidencia del mismo y se acepte el PopUp,
	 * dejando la marca de su existencia en [DatosEmpresarial.ALERTA_ACH]
	 */
	public String waitPantallaConfirmar(String pageTitle) throws Exception {

		DatosEmpresarial.ALERTA_ACH = false; // VALOR POR DEFECTO
		WebElement elementPaso;
		String msgError = null;
		try {
			do { // ESPERA A QUE MUESTRE LA PANTALLA DE CONFIRMAR O ALERTA
				DXCUtil.wait(5);
				this.existDialogAccept();
				elementPaso = this.element(locPasoPage);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");
				Evidence.saveAllScreens("Pantalla Confirmación", this);
			} while (elementPaso == null && msgError == null);
			if (elementPaso == null && msgError != null) {
//				Evidence.save("ErrorValores", this);
				Evidence.save("ErrorValores");
				return msgError;
			}
			// SI LLEGA A ESTE PUNTO ESTÁ EN LA PANTALLA DE CONFIRMACIÓN, AVECES EL POPUP
			// ACH SE DEMORA
			DXCUtil.wait(3);
			if (this.existDialog()) {
				DatosEmpresarial.ALERTA_ACH = this.getMessageDialog().contains("que realice a otros bancos");
				DXCUtil.wait(3);
				Evidence.saveWithDialog("AlertaACH", this, pageTitle);
				this.existDialogAccept();
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		catch (UnhandledAlertException e) { // SE PRESENTÓ ALERTA Y DEBERÍA SER LA DE ACH
			if (this.existDialog()) {
				msgError = this.getMessageDialog();
				DatosEmpresarial.ALERTA_ACH = msgError.contains("que realice a otros bancos");
			}
			if (DatosEmpresarial.ALERTA_ACH) {
				Evidence.saveWithDialog("AlertaACH", this, pageTitle);
				msgError = null;
			} else
				Evidence.saveWithDialog("AlertaDesconocida", this, pageTitle);
			this.acceptDialog();
		}
//-----------------------------------------------------------------------------------------------------------------------
//		this.costoTransaccion = null;
//		WebElement elementCostoTx = this.element(locCostoTx);
//		if (this.element(elementCostoTx) != null)
//			this.costoTransaccion = DXCUtil.toNumberInString(elementCostoTx.getText(), 2);

		return msgError;
	}
//***********************************************************************************************************************

}