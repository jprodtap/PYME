package dav.pymes.moduloTx;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageAdminParametros extends PagePortalPymes {

	PageOrigen pageOrigen = null;

	By locNumAprobac = By.id("cphCuerpo_ddlAprobacionEjecutar"); // SELECT
	By locTipoAbono = By.id("cphCuerpo_ddlAbonosPago"); // SELECT
	By locCtaInscrta = By.id("cphCuerpo_ddlTransferencias"); // SELECT
	By bienvenido = By.xpath("//div[@id='cphCuerpo_PnlPrincipal']//tbody//tr[@class='Titulo']");

//=======================================================================================================================
	/**
	 * Constructor a partir del [BasePageWeb] padre.
	 */
	public PageAdminParametros(PageLoginPymes parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Este m�todo hace la configuraci�n en par�metros generales, y guarda la
	 * evidencia.
	 */
	public String hacerConfiguracion(String numAprobaciones, String tipoAbono, String ctaInscrita) throws Exception {

		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
		} while (this.element(bienvenido) == null && contador < 4);

		if (this.isDisplayed(bienvenido)) {
			WebElement getexto = this.findElement(bienvenido);
			String texto = this.getText(getexto);
			if (texto.contains("Bienvenido al Portal PYME DAVIVIENDA")) {
				this.clickButton("Acepto");
			}
		}

		this.pageOrigen = new PageOrigen(this);
		
		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		
		if (navegador.contains("CHROME")) {
			DXCUtil.wait(3);
			this.pageOrigen.irAOpcion(null, "Administración", "Administración Portal", "Parametros Generales");
			
		}else {
			DXCUtil.wait(6);
			this.pageOrigen.irAOpcionMoZilla(null, "Administración", "Administración Portal","Datos de usuario", "Parametros Generales");
		}

		boolean huboCambio = false;

		do {

			DXCUtil.wait(1);
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut No se encontro el campo: Titulo");
//				Evidence.save("TimeOut", this);
				Evidence.save("TimeOut");
				return "TimeOut No se encontro el campo: Titulo";
			}
		} while (this.element("//*[@id='lblMasterMigajasPan']/a") == null);

		this.ValidacionPreguntasPep();
//		Evidence.save("Parametros Generales", this);
		Evidence.save("Parametros Generales");
		huboCambio |= validarParametro(locNumAprobac, numAprobaciones);
		
		if (tipoAbono != null || !tipoAbono.isEmpty() || !tipoAbono.contains(" ")) {
		String tipoAbonoSelecc ="uno a uno";
		if (tipoAbono.contains("uno a uno"))
		tipoAbonoSelecc = "uno a uno";
		if (tipoAbono.contains("uno a varios"))
			tipoAbonoSelecc = "uno a varios";
		huboCambio |= validarParametro2(locTipoAbono, tipoAbonoSelecc);
		}
		
		String ctaInscrSelecc = "SI"; // PARA EVITAR QUE LLEVE TILDE
		if (ctaInscrita != null && ctaInscrita.toUpperCase().equals("NO"))
			ctaInscrSelecc = "NO";
		huboCambio |= validarParametro(locCtaInscrta, ctaInscrSelecc);

		if (huboCambio)
			this.clickButton("Guardar Cambios");

		String msgAlerta;
		do { // ESPERA A QUE MUESTRE LA PANTALLA DE VALORES O UN ALERTA
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (msgAlerta == null);

		if (msgAlerta == null)
			return null;

		Evidence.saveAllScreens("ParamsGenerales", this);
		return msgAlerta;
	}

	private boolean validarParametro(By locator, String valor) throws Exception {
		boolean huboCambio = false;
		int contador = 0;
		if (valor != null) {
			do {
				DXCUtil.wait(1);
				contador++;
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el : Modulo");
					SettingsRun.exitTestIteration();
				}
			} while (!this.isDisplayed(locator));
			if (this.getItemSelected(locator) != valor) {
				String msg = this.selectListItemExacto(locator, valor);
				if (!msg.isEmpty()) {
				Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
				huboCambio = true;
			}
		}
		return huboCambio;
	}
	private boolean validarParametro2(By locator, String valor) throws Exception {
		boolean huboCambio = false;
		int contador = 0;
		if (valor != null) {
			do {
				DXCUtil.wait(1);
				contador++;
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el : Modulo");
					SettingsRun.exitTestIteration();
				}
			} while (!this.isDisplayed(locator));
			if (this.getItemSelected(locator) != valor) {
				String msg = this.selectListItem(locator, valor);
				if (!msg.isEmpty()) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
				huboCambio = true;
			}
		}
		return huboCambio;
	}

//***********************************************************************************************************************
}