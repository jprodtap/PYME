package dav.AdministracionDelEfectivo;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.NotificacionSMS;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageConfiAproAdef extends BasePageWeb {
	
	public PageConfiAproAdef(BasePageWeb parentPage) {
		super(parentPage);
	}
	
	PageConfirmacion pageConfirmacion = null;
	PageAdministracionDelEfectivo pageAdminEfectivo = null;
	
	Date fechaHoraTx;
	
	By table = By.xpath("//*[@id=\"app\"]/div/div/div[8]/div[3]/input[1]");
	By nbButtonAproAdef = By.xpath("//*[@id=\"app\"]/div/div/div[8]/div[2]/input[1]");
	By BtnSeguroAproAdef = By.xpath("//*[@id=\"procesando\"]/div/div/div[2]/button[2]");
	By locCmMsgTokenOtpAdef = By.xpath("//*[@id=\"app\"]/div/div/div[2]/div[2]/pre");//*[@id="app"]/div/div/div[1]/div[2]/pre/text()[1]
	By locCmMsgTokenOtpAdefText = By.xpath("//*[@id=\"app\"]/div/div/div[2]/div[2]/pre/text()[1]");
	By locCmMsgTokenOtpAdefPend = By.xpath("//*[@id=\"app\"]/div/div/div[1]/div[2]/pre");
	By locNumAproba = By.xpath("(//span[@class=\"num-doc\"])[2]");
	
	// ***********************************************************************************************************************
		/**
		 * La pantalla se encuentra en ingresar OTP/Token. Ingresa OTP/Token le da al
		 * boton confirmar. aprueba la transaccion de Ordenes de Pago.
		 */

		public String aprobarTxADEF(boolean primeroguardar) throws Exception {

			boolean isCliVirtual = DatosEmpresarial.TIPO_TOKEN.equals(DatosEmpresarial.TOKEN_OTP);
			this.existDialogAccept();
			DXCUtil.wait(4);

			NotificacionSMS.loadHour(); // SIRVE PARA OTP
			String msgResp = null;
			WebElement objToken = null;
			boolean estaHabilitado = true;

			do {
			} while (!this.isDisplayed(nbButtonAproAdef));
				
			if (this.element(nbButtonAproAdef) != null) {
				DXCUtil.wait(1);
				this.click(nbButtonAproAdef); 
			}			
			
			DXCUtil.wait(12);
			
			if (this.element(BtnSeguroAproAdef) != null) {
				DXCUtil.wait(1);
				this.click(BtnSeguroAproAdef);
			}
			
			// -----------------------------------------------------------------------------------------------------------------------
			if (estaHabilitado) { // SE PUEDE INGRESAR EL TOKEN / OTP
				if (true) {
					this.moveUpScreen();
					// Mod Para Interna
					Evidence.saveAllScreens("MsgEnvíoOtp", this);
				}
				DXCUtil.wait(1);
				
				this.pageConfirmacion = new PageConfirmacion(this);
				msgResp = this.pageConfirmacion.ingresarDinamica(0);

			} else if (!estaHabilitado && DatosDavivienda.IS_RISKMOTOR) {
				this.fechaHoraTx = new Date();

				// -----------------------------------------------------------------------------------------------------------------------
				// Mod Para Interna
				Evidence.save("AprobarTxh");
				Evidence.saveAllScreens("AprobarTx", this);
				
			}

			if (msgResp.equals("") && primeroguardar) {

				Evidence.save("mensaje de confirmar aprobacion");
				this.click(By.xpath("//*[@id=\"app\"]/div/div/div[3]/div[3]/input[2]"));

				WebElement msgAdef = this.element(locCmMsgTokenOtpAdefPend);

				do {
				} while (!this.isDisplayed(this.element(locCmMsgTokenOtpAdefPend)));
				
				Evidence.save("Mensaje aprobacion");
				
			} else if (msgResp.equals("") && !primeroguardar) {
				
				Evidence.save("mensaje de confirmar aprobacion");
				this.click(By.xpath("//*[@id=\"app\"]/div/div/div[8]/div[3]/input[2]"));

				do {
				} while (!this.isDisplayed(this.element(locCmMsgTokenOtpAdef)));
				
				DXCUtil.wait(1);
				
				WebElement msgAdef = this.findElement(By.cssSelector("pre.texto-msj"));
				
				msgResp = msgAdef.getText();
				
				WebElement numAprobacion = this.element(locNumAproba);
				String numApro = numAprobacion.getText();
				
				this.pageAdminEfectivo = new PageAdministracionDelEfectivo(this);
				pageAdminEfectivo.numAprob = numApro;
				
				Evidence.save("Mensaje aprobacion");
			}

			return msgResp;
		}
	
//	public String aprobarTxPendAdef() throws Exception {

}

//		String msgError = null;
//
//		String pagoyTx = this.pageOrigen.getTextMid();
//
//		if (pagoyTx.contains("Pagos, Transferencias y otros")) {
//			msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos, Transferencias y otros",
//					"Pendientes de Aprobaci�n");
//		} else if (pagoyTx.contains("Pagos, Transferencias y Otros")) {
//			msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos, Transferencias y Otros",
//					"Pendientes de Aprobaci�n");
//
//		} else {
//			msgError = pageOrigen.irAOpcion("Transacciones Pendientes de Firmas", "Pagos y Transferencias",
//					"Pendientes de Aprobaci�n");
//		}
//		if (msgError != null)
//			this.terminarIteracionXError(msgError);
//
//		String numDocumento = this.numAprova;
//		String numCodSeg = null; // @PEDRO FALTA CVV
//		int numRetosFalla = 0;
//		if (DatosDavivienda.IS_RISKMOTOR)
//			numRetosFalla = Integer.valueOf(SettingsRun.getGlobalData().getParameter("Ingresos Fallidos").trim());
//
//		if (desdeDetalle) {
//			msgError = pagePendApr.entrarDetalleTx(numDocumento);
//			if (msgError != null)
//				this.terminarIteracionXError(msgError);
//
////			pageConf.setNbButtonAprobar("Aprobar Transacci�n");
//		} else { // DESDE CAJA
//			pagePendApr.seleccionarTransaccion(numDocumento);
//			pageConf.setNbButtonAprobar("Aprobar Transacciones Seleccionadas");
////			pagePendApr.ingresarCodSeguridad(numCodSeg);
//		}
////			String msgRta = pageConf.aprobarTx(numRetosFalla, numCodSeg);
//		String msgRta = pageConf.aprobarTx(numRetosFalla, desdeDetalle);
//
//		if (DatosDavivienda.IS_RISKMOTOR) {
//
//			this.estadoTx = msgRta;
//			this.getEstado(msgRta);
//			if (DXCUtil.itemContainsAnyArrayItem(msgRta, arrMsgBuscarEstado)) {
//				this.estadoTx = "Transferencia Realizada";// @pedro buscar estado de la transaccion
//				Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
//			} else if (DXCUtil.itemContainsAnyArrayItem(msgRta, this.arrMsgTxDeclinada)) {
//				Reporter.reportEvent(Reporter.MIC_INFO, msgRta);
//				this.estadoTx = "DECLINADA";
//				if (DatosDavivienda.IS_RISKMOTOR) {
//					this.valorTx = pageConf.getValorFinalTx();
//					this.bancoDesTx = pageConf.getBancoDesTx();
//
//					getBancoDesTx(this.bancoDesTx);
//					getActivityAmount(this.valorTx.replace("$", "").replace(",", "").replace(" ", ""));
//				}
//				this.numeroTx = pageConf.getNumeroDocumento();
//				getNumApr(this.numeroTx);
//				if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
//						|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
//					this.validarIngresoMRDestinosMasivo();
//				} else {
//
//					this.validarIngresoMR();
//
//				}
//				this.pageOrigen.terminarIteracion();
//			}
//			if (this.servicio.contains("N�mina") || this.servicio.contains("Pago a Proveedores")
//					|| this.servicio.contains("AFC") || this.servicio.contains("Cr�dito.3ros")) {
//				this.validarIngresoMRDestinosMasivo();
//			} else {
//
//				this.validarIngresoMR();
//
//			}
//		}
//
//		if (!msgRta.contains(PageConfirmacion.MSG_EXITO_PAGO) && !msgRta.contains(PageConfirmacion.MSG_EXITO_GUARD)
//				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_ENV_OTP)
//				&& !msgRta.contains(PageConfirmacion.MSG_EXITO_APROB))
//			this.terminarIteracionXError(msgRta);
//
//	}

	

