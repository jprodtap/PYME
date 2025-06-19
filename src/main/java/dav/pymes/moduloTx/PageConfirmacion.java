package dav.pymes.moduloTx;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.NotificacionSMS;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.util.DXCUtil;

public class PageConfirmacion extends PagePortalPymes {

	public final static String MSG_EXITO_GUARD = "Esta transacci�n"; // INDICA QUE FALTA FIRMA
	public final static String MSG_EXITO_ENV_OTP = "Para confirmar la transacci�n"; // INDICA QUE ENVI� OTP
	public final static String MSG_EXITO_APROB = "Usted aprob�"; // APROBACI�N DESDE CAJA
	public final static String MSG_EXITO_PAGO = "Su transacci�n ha sido recibida por el Portal"; // 1FIRMA / APROBACI�N
																									// DESDE DETALLE
	public final static String MSG_EXITO_DECL1 = "Transacci�n declinada por seguridad"; // DECLINACI�N 1FIRMA:APROBACI�N
																						// DESDE DETALLE
	public final static String MSG_EXITO_DECL2 = "Sus transacciones han sido declinadas"; // DECLINACI�N : APROBACI�N
																							// DESDE CAJA
	public static final String MSG_ERROR_NO_OTP = "NO SE RECIBI� LA OTP, SE TERMINA LA PRUEBA";

	// MENSAJES QUE INDICAN SI LA ACCI�N DE CONFIRMACI�N FUE ENVIADA Y QUE GENERAN
	// N�MERO DE DOCUMENTO (SI NO SE TIENE)
	public final static String[] ARR_MSG_ACT_SENT = { MSG_EXITO_GUARD, MSG_EXITO_APROB, MSG_EXITO_PAGO, MSG_EXITO_DECL1,
			MSG_EXITO_DECL2 };

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Confirmar']");
	By locBtGuardar = By.xpath("//input[@id='cphCuerpo_btnGuardarSinAprobar']");
	By locBtAprobar = By.xpath("//input[@id='cphCuerpo_btnAprobarTransaccion']");
	By locCmTokenOtp = By.xpath("//input[@id='cphCuerpo_tbxTokenOtp' or @id='cphCuerpo_tbxTokenOtpApEl']");
	By locCmCvvDet = By.xpath("//input[@id='cphCuerpo_tbxCodigoSeguridadApEl']");
	By locCmCvvCrea = By.xpath("//div[@id='cphCuerpo_divC']/input");
	By locNumDoc = By.xpath("//span[@id='cphCuerpo_lblNumeroDocumento' or @id='cphCuerpo_lb_numeroDocumento']");
	By locCostoTx = By.xpath("//span[@id='cphCuerpo_lblcosto' or @id='cphCuerpo_lblInformacionDaviviendaCosto'"
			+ " or @id='cphCuerpo_lblCostoTransaccion' or @id='cphCuerpo_lblCosto']");
	By locLinkTxAprob = By.partialLinkText("Consultas y Extractos.");

	private String costoTransaccion;
	private Date fechaHoraTx;
	private String nbButtonAprobar = "Aprobar Transacci�n";
	private boolean isRiskAlto = false;

//=======================================================================================================================	
	public PageConfirmacion(PageLoginPymes parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	public String getCostoTransaccion() {
		return this.costoTransaccion;
	}

	public Date getFechaHoraTx() {
		return this.fechaHoraTx;
	}

	public void setNbButtonAprobar(String nbButtonAprobar) {
		this.nbButtonAprobar = nbButtonAprobar;
	}

	public void setIsRiskAlto(boolean isRiskAlto) {
		this.isRiskAlto = isRiskAlto;
	}

//***********************************************************************************************************************
	/**
	 * La pantalla se encuentra en "Confirmar". Da click en el bot�n "Guardar sin
	 * aprobar" y retorna el mensaje que se presenta.
	 */
	public String guardarSinAprobar() throws Exception {
		this.click(locBtGuardar);
		Evidence.saveFullPage("GuardarSinAprobar", this);
		return this.getMsgAlertIfExist("lblMasterAlerta");
	}
//***********************************************************************************************************************

//***********************************************************************************************************************
	/**
	 * La p�gina Web se encuentra en alguna de las pantallas que permiten clickear
	 * la opci�n para aprobar la transacci�n (nombre del bot�n =
	 * [this.nbButtonAprobar]):<br>
	 * - Pantalla 'Confirmar' al final de la creaci�n de la transacci�n.<br>
	 * - Pantalla 'Detalle de Transacci�n' cuando se ingresa al detalle de una
	 * transacci�n pendiente de aprobaci�n para aprobarla.<br>
	 * - Pantalla 'Transacciones Pendientes de Firmas' cuando se selecciona del
	 * listado de transacciones pendientes de aprobaci�n una transacci�n para
	 * aprobarla. <br>
	 * <br>
	 * Si [seEsperaMsgACH] es [true] y si el campo del token a�n no est� activo,
	 * este m�todo espera hasta 7 segundos a que se presente el popup de ACH,
	 * dejando la respectiva evidencia y marca en [DatosEmpresarial.ALERTA_ACH].<br>
	 * Despu�s espera la habilitaci�n del campo token o la muestra de un mensaje de
	 * error, en este �ltimo caso el error es retornado.<br>
	 * En caso que se presente un popup no esperado [UnhandledAlertException],
	 * eval�a si es el Popup de ACH, no se puede tomar evidencia en tal caso, porque
	 * el catch de la Excepci�n indica que ya cierra la alerta, por ende s�lo se
	 * comunica a trav�s de un [Reporter.MIC_INFO]. <br>
	 * <br>
	 * Este m�todo controla el n�mero de retos de falla que se indiquen en
	 * [numRetosFalla].<br>
	 * <b>Retorna el mensaje generado por el intento de aprobaci�n, no se sabe si es
	 * de �xito o de falla.</b><br>
	 * Si el link para ir a la consulta de transacciones se presenta, este m�todo
	 * deja la p�gina en la pantalla "Transacciones Realizadas en el Portal PYME".
	 */
	public String aprobarTx(String valCodSeg, int numRetosFalla, boolean seEsperaMsgACH, String pageTitle)
			throws Exception {

		if (seEsperaMsgACH)
			DatosEmpresarial.ALERTA_ACH = false; // ASUME QUE NO SE MUESTRA ALERTA DE ACH
		boolean isCliVirtual = DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP);
//-----------------------------------------------------------------------------------------------------------------------
		// SI AL INGRESAR : EL CAMPO DEL TOKEN / OTP NO EST� HABILITADO ES PORQUE DEBE
		// DAR CLICK EN EL BOT�N DE
		// APROBAR TRANSACCI�N PARA QUE SE HABILITE
		boolean estaHabilitado = this.isEnabled(this.element(locCmTokenOtp));
		String msgResp = null;
		if (!estaHabilitado) {
			NotificacionSMS.loadHour(); // SIRVE PARA OTP
			this.fechaHoraTx = new Date(); // PUEDE SALIR ERROR POR DECLINACI�N, SE TOMA EL DATO DEL TIME DE LA TX
			this.clickButton(this.nbButtonAprobar);
//-----------------------------------------------------------------------------------------------------------------------
			if (seEsperaMsgACH) // DEBE MOSTRAR EL DI�LOGO DE ACH, SE DA UNA ESPERA DE 7 SEGUNDOS M�XIMO
				this.esperaPopupACH(pageTitle);
//-----------------------------------------------------------------------------------------------------------------------
			try {
				do {
					msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
					estaHabilitado = this.isEnabled(this.element(locCmTokenOtp));
				} while (msgResp == null && !estaHabilitado);
			} catch (UnhandledAlertException e) { // SE PRESENT� ALERTA NO CONTROLADA : EL DI�LOGO SE DEBI� CERRAR
				String msgError = e.getAlertText();
				Reporter.reportEvent(Reporter.MIC_INFO, "Sali� alerta [" + msgError
						+ "] - No se pudo guardar evidencia porque se reconoci� en Excepci�n");

				if (!DatosEmpresarial.ALERTA_ACH)
					DatosEmpresarial.ALERTA_ACH = msgError.contains("que realice a otros bancos");

				msgResp = this.getMsgAlertIfExist("lblMasterAlerta");
				estaHabilitado = this.isEnabled(this.element(locCmTokenOtp));
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		if (estaHabilitado) { // SE PUEDE INGRESAR EL TOKEN / OTP
			// SI ES MOTOR DE RIESGO CON RIESGO ALTO, NO DEBER�A PEDIR DIN�MICA, SI LO HACE
			// RETORNA EL MENSAJE
			if (DatosDavivienda.IS_RISKMOTOR && this.isRiskAlto) {
				this.moveToElement(this.element(locCmTokenOtp)); // MUEVE AL CAMPO DE LA DIN�MICA
				String dato = "TOKEN";
				if (isCliVirtual)
					dato = "OTP";
//				Evidence.save("Error_Solicita" + dato, this);
				Evidence.save("Error_Solicita" + dato);
				return "SOLICIT� " + dato + " PARA UN CLIENTE RIESGO ALTO";
			}
			if (isCliVirtual) {
				this.moveUpScreen();
//				Evidence.save("MsgEnv�oOtp", this);
				Evidence.save("MsgEnv�oOtp");
			}
			msgResp = this.ingresarDinamica(numRetosFalla, valCodSeg);
		}
//-----------------------------------------------------------------------------------------------------------------------
		Evidence.saveFullPage("RtaAprobacion", this);
		WebElement objLinkConsulta = this.element(locLinkTxAprob);
		if (objLinkConsulta != null)
			this.click(objLinkConsulta);

		return msgResp;
	}

//***********************************************************************************************************************
	/**
	 * Retorna el n�mero de documento que se presenta en la pantalla de
	 * Confirmaci�n. En caso que no se presente el retorno es [null].
	 */
	public String getNumeroDocumento() {
		String numeroDocumento = null;
		WebElement elementDoc = this.element(locNumDoc);
		if (elementDoc != null)
			numeroDocumento = elementDoc.getText();
		return numeroDocumento;
	}

//***********************************************************************************************************************
	/**
	 * Si [seEsperaMsgACH] es [true] este m�todo espera hasta 7 segundos a que se
	 * presente el popup de ACH, dejando la respectiva evidencia y marca en
	 * [DatosEmpresarial.ALERTA_ACH].<br>
	 * Despu�s espera la pantalla confirmar, puede pasar que se presente un error,
	 * en tal caso este error es retornado.<br>
	 * En caso que se presente un popup no esperado [UnhandledAlertException],
	 * eval�a si es el Popup de ACH, no se puede tomar evidencia en tal caso, porque
	 * el catch de la Excepci�n indica que ya cierra la alerta, por ende s�lo se
	 * comunica a trav�s de un [Reporter.MIC_INFO].
	 */
	public String waitPantallaConfirmar(boolean seEsperaMsgACH, String pageTitle) throws Exception {

		DatosEmpresarial.ALERTA_ACH = false; // VALOR POR DEFECTO
		if (seEsperaMsgACH) // DEBE MOSTRAR EL DI�LOGO DE ACH, SE DA UNA ESPERA DE 7 SEGUNDOS M�XIMO
			this.esperaPopupACH(pageTitle);
//-----------------------------------------------------------------------------------------------------------------------
		WebElement elementPaso;
		this.costoTransaccion = null;
		String msgError = null;
		try {
			do { // ESPERA A QUE MUESTRE LA PANTALLA DE CONFIRMAR O ALERTA
				elementPaso = this.element(locPasoPage);
				msgError = this.getMsgAlertIfExist("lblMasterAlerta");
			} while (elementPaso == null && msgError == null);
			if (elementPaso == null && msgError != null) {
//				Evidence.save("ErrorValores", this);
				Evidence.save("ErrorValores");
				return msgError;
			}
			WebElement elementCostoTx = this.element(locCostoTx);
			if (elementCostoTx != null)
				this.costoTransaccion = DXCUtil.toNumberInString(elementCostoTx.getText(), 2);
		}
//-----------------------------------------------------------------------------------------------------------------------
		catch (UnhandledAlertException e) { // SE PRESENT� ALERTA NO CONTROLADA : EL DI�LOGO SE DEBI� CERRAR
			msgError = e.getAlertText();
			Reporter.reportEvent(Reporter.MIC_INFO,
					"Sali� alerta [" + msgError + "] - No se pudo guardar evidencia porque se reconoci� en Excepci�n");

			if (!DatosEmpresarial.ALERTA_ACH)
				DatosEmpresarial.ALERTA_ACH = msgError.contains("que realice a otros bancos");

			WebElement elementCostoTx = this.element(locCostoTx);
			if (elementCostoTx != null)
				this.costoTransaccion = DXCUtil.toNumberInString(elementCostoTx.getText(), 2);
		}
//-----------------------------------------------------------------------------------------------------------------------
		return msgError;
	}

//***********************************************************************************************************************
	/**
	 * Se espera que presente el popup de ACH por tener destinos a otros bancos. La
	 * espera m�xima es de 7 segundos.<br>
	 * Si se presenta almacena la evidencia.
	 */
	private void esperaPopupACH(String pageTitle) throws Exception {
		int timeEspera = 0;
		do {
			DXCUtil.wait(4);
			if (this.existDialog()) {
				DatosEmpresarial.ALERTA_ACH = this.getMessageDialog().contains("que realice a otros bancos");
				// Evidence.saveWithDialog("AlertaACH", this, pageTitle);
				this.acceptDialog();
				break; // PARA QUE SALGA DEL CICLO
			}
			timeEspera++;
		} while (timeEspera <= 7); // M�XIMO DE ESPERA 7 SEGUNDOS
	}

//***********************************************************************************************************************
	/**
	 * M�todo que ingresa la din�mica (sea Token u Otp).<br>
	 * El retorno nuna ser� [null], dar� la respuesta del portal al ingresar la
	 * din�mica, en este punto no se sabe si es una respuesta de �xito o de falla.
	 */
	private String ingresarDinamica(int numRetosFalla, String valCodSeg) throws Exception {

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
				lastNum++; // PARA QUE SEA UN N�MERO DIFERENTE DEL �LTIMO D�GITO DEL TOKEN / OTP
				arrTokIngresar[posArr] = DXCUtil.left(arrOtps[0], longTok - 1)
						+ DXCUtil.right(String.valueOf(lastNum), 1);
			}
			arrTokIngresar[numRetosFalla] = arrOtps[0]; // DE �LTIMAS SE DEJA EL CORRECTO
		}
//-----------------------------------------------------------------------------------------------------------------------
		WebElement objCvv = this.element(locCmCvvCrea);
		if (objCvv == null)
			objCvv = this.element(locCmCvvDet);
		boolean hayRpta;
		String valDinamica, msgRta = null;
		int numIngresos = 0;

		for (int posArr = 0; posArr < arrTokIngresar.length; posArr++) {

			if (!this.isEnabled(locCmTokenOtp))
				break; // NO PUEDE INGRESAR DIN�MICA

			if (posArr > 0) { // HAB�A M�S DE 1 TOKEN / OTP, GUARDA EVIDENCIA DEL ANTERIOR
				this.moveUpScreen();
//				Evidence.save(dato + "(" + posArr + ")Respuesta", this);
				Evidence.save(dato + "(" + posArr + ")Respuesta");
			}

			this.moveDownScreen();
			this.write(locCmTokenOtp, arrTokIngresar[posArr]);
			if (objCvv != null) // PIDE CVV
				this.write(objCvv, valCodSeg);
//			Evidence.save(dato + "(" + (posArr + 1) + ")Ingreso", this);
			Evidence.save(dato + "(" + (posArr + 1) + ")Ingreso");

			this.fechaHoraTx = new Date(); // ESTA ES LA FECHA Y HORA DE LA TRANSACCI�N, SI SE REALIZA
			this.clickButton("Confirmar Transacci�n");
			numIngresos++;
//-----------------------------------------------------------------------------------------------------------------------
			do { // ESPERA RESPUESTA DEL INGRESO DEL TOKEN, SE SABE QUE HUBO RESPUESTA PORQUE:
					// CAMPO DE LA DIN�MICA YA NO EST� O CAMPO DE LA DIN�MICA EST� EN VAC�O
				hayRpta = (this.element(locCmTokenOtp) == null);
				if (!hayRpta) { // EXISTE EL CAMPO DE LA DIN�MICA
					valDinamica = this.getText(locCmTokenOtp);
					hayRpta = (valDinamica != null && valDinamica.isEmpty());
				}
			} while (!hayRpta);
			msgRta = this.getMsgAlertIfExist("lblMasterAlerta"); // SIEMPRE HAY MENSAJE
//-----------------------------------------------------------------------------------------------------------------------
		} // FOR
		if (numRetosFalla != 0 && numRetosFalla + 1 != numIngresos) // PIDE NEXT TOKEN
			msgRta += "\nPERO NO REALIZ� LOS RETOS SOLICITADOS : [" + numRetosFalla + "]";
		return msgRta;
	}
//***********************************************************************************************************************
}