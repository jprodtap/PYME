package dav.pymes.moduloTx;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
import dxc.util.DXCUtil;

public class PageValores extends PagePortalPymes {

	By locPasoPage  = By.xpath("//td[@class='TituloRojo']/b[text()='Valores']");
	By locButtonSig = By.xpath("//input[contains(@value, 'Siguiente')]");
	// LOCATOR DESCRIPCIÓN PARA ANÓMINA Y PROVEEDORES
	By locCmDescrNP = By.id("cphCuerpo_txtVCdescripcion");
	// LOCATORS PARA DATOS DE OTRAS TRANSACCIONES
	By locCmValor = By.xpath("//input[@id='cphCuerpo_txtValor']");
	By locCmDescr = By.xpath("//input[@id='cphCuerpo_txtDescripcion']");
	By locCmRefer = By.xpath("//input[@id='cphCuerpo_txtReferencia' or @id='cphCuerpo_txtreferencia']");
	// LOCATORS VALOR PAGO PARA SERVICIOS PÚBLICOS
	By locCmValorFact1 = By.xpath("//input[@id='cphCuerpo_txtValorAPagar' or @id='cphCuerpo_TxtValorAPagar']");
	By locCmValorFact2 = By.xpath("//span[@id='cphCuerpo_txtValorPagar']");
//=======================================================================================================================
	/**
	 * Constructor a partir del [BasePageWeb] padre.
	 */
	public PageValores(PageLoginPymes parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Pantalla donde sólo solicita la descripción, por lo general pantalla para Nómina y Proveedores.<br>
	 * Este método ingresa el dato y da click en el botón "Siguiente", no valida si sale o no error después.
	 */
	public void ingresarDescripcion(String descripcion) throws Exception {
		
		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		this.write(locCmDescrNP, descripcion);
		Evidence.saveFullPage("Descripcion", this);
		this.click(locButtonSig);
	}
//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida si sale o no error después.
	 */
	public void ingresarValores(String valor, String descripcion, String referencia) throws Exception {
		
		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		this.write(locCmValor, valor);
		this.write(locCmDescr, descripcion);
		this.write(locCmRefer, referencia);
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
	}
//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita o NO el valor, por lo general pantalla para Servicios.<br>
	 * Este método ingresa el dato (si se pide) y da click en el botón "Siguiente", no valida si sale o no error después.<br>
	 * Retorna el valor de la transacción, en caso que el dato se presente y no pida ingreso.
	 */
	public String ingresarValor(String valor) throws Exception {
		
		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		
		WebElement objCmValor = this.element(locCmValorFact1); // CAMPO DE ENTRADA = INPUT
		if (objCmValor != null) // SE DEBE INGRESAR EL VALOR
			this.write(objCmValor, valor);
		else {
			objCmValor = this.element(locCmValorFact2); // CAMPO CON EL VALOR
			valor = DXCUtil.toNumberInString(this.getText(objCmValor), 2);
		}
//		Evidence.save("ValorTx", this);
		Evidence.save("ValorTx");
		this.click(locButtonSig);
		return valor;
	}
//***********************************************************************************************************************
	/**
	 * Método para esperar a que la pantalla Web se encuentre en "Valores".
	 */
	private void waitPantallaValores() {
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA "Valores"
			elementPaso = this.element(locPasoPage);
		} while (elementPaso == null);

	}
//***********************************************************************************************************************
}