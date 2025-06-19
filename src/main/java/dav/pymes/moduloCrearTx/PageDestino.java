package dav.pymes.moduloCrearTx;

import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PagePortalPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageDestino extends PagePortalPymes {

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Destino']");
	By locPasoPageValor = By.xpath("//td[@class='TituloRojo']/b[text()='Valores']");
	String xPathRBDestino = "(//td[contains(text(),'NUM_PROD')]//preceding-sibling::td[contains(text(),'TIPO_PROD')]//preceding-sibling::td/input)[1]";
	String xPathRBDestinoFic = "//td[contains(text(),'NUM_PROD')]//preceding-sibling::td/input";
	By locTipoPruebaFic = By.xpath("./parent::td/following-sibling::td[1]");
	By locButtonSig = By.xpath("//input[contains(@value, 'Siguiente')]");
	By locReferencia1 = By.id("cphCuerpo_tx_Referencia1");
	By locReferencia2 = By.id("cphCuerpo_tx_Referencia2");
	By locReferencia1Auto = By.id("cphCuerpo_tbReferencia1");
	By locReferencia2Auto = By.id("cphCuerpo_tbReferencia2");
	By locNombreArecordar = By.id("cphCuerpo_tbNombreRecordar");
	// LOCATOR PARA INGRESAR EL DESTINO CUENTA NO INSCRITA
	By locBandest = By.id("cphCuerpo_ddlBancoDestino");
	By locTipctadest = By.id("cphCuerpo_ddlDestinoTipoID");
	By locNumdest = By.id("cphCuerpo_txtDestinoNumero");
	By locNomtitu = By.id("cphCuerpo_txtDestinoNombreTitular"); // TAMBI�N FUNCIONA PARA N�MINA Y PROVEEDORES
	By locTipid = By.id("cphCuerpo_ddIDestinoTipoIDTitular");
	By locNumid = By.id("cphCuerpo_txtDestinoIDNumTitular");

	By veri = By.xpath("//input[@type='submit'][@value='Continuar con errores']");

	// LOCATOR PARA INGRESAR EL DESTINO PAGO DE N�MINA O PREOVEEDORES
	By locBanDestNP = By.id("cphCuerpo_dropDestinoBanco");
	By locTipCtaDestNP = By.id("cphCuerpo_dropDestinoTipoCuenta");
	By locNumDestNP = By.id("cphCuerpo_txtDestinoNProducto");
	By locTipIdNP = By.id("cphCuerpo_dropDestinoTipoIdentificacion");
	By locNumIdNP = By.id("cphCuerpo_txtDestinoNumeroIdentificacion");
	By locValorPagoNP = By.id("cphCuerpo_txtDestinoValorPago");
	By locReferNP = By.id("cphCuerpo_txtDestinoReferencia");
	// LOCATOR PARA INGRESAR EL DESTINO PAGO AFC
	By desnumeroCuenta = By.id("cphCuerpo_txtNumeroCuenta");
	By destTitular = By.id("cphCuerpo_txtDestinoNombreTitular");
	By destnuId = By.id("cphCuerpo_txtDestinoNumeroIdentificacion");
	By destValPago = By.id("cphCuerpo_txtDestinoValorPago");
	By destValContin = By.id("cphCuerpo_txtDestinoValorContin");
	By adicionarCuenta = By.xpath("//input[@id='cphCuerpo_btnDestinoAdicionarCuenta']");
	By bntContinuarConErr = By.xpath("//input[@id='cphCuerpo_btnContinuarErrores']");

	// LOCATOR PARA INGRESAR EL DESTINO PAGO Pagos a cr�ditos de terceros

	By lisdestinoTipoProducto = By.id("cphCuerpo_dropDestinoTipoProducto");
	By campDesNumeroCuenta = By.id("cphCuerpo_txtNumeroCuenta");
	By campDestTitular = By.id("cphCuerpo_txtDestinoNombreTitular");
	By campDestIdentificacion = By.id("cphCuerpo_txtDestinoNumeroIdentificacion");
	By campDestinoValorPago = By.id("cphCuerpo_txtDestinoValorPago");

	// LOCATOR PARA EL FILTRO EN CUENTA INSCRITA
	By locbandesctains = By.id("cphCuerpo_ddlBanco");
	By locNumdesctains = By.id("cphCuerpo_txtNumeroDestino");
	By locBtnbuscar = By.id("cphCuerpo_btnBuscar");
	// LOCATOR CARGA DE ARCHIVO (PAGOS MASIVOS)
	
	By locFormatoArchivo = By.id("cphCuerpo_dropDestinoFormato");
	By locFileCarga = By.id("cphCuerpo_fuDestinoArchivo"); // INPUT TYPE=FILE
	By locLinkError = By.linkText("aquí");
	By locLinkErrorclick = By.xpath("//*[@id='lblMasterAlerta']/a");
	/*
	 * String xPathRBDestinoins = "\r\n" +
	 * "//td[contains(text(),'NUM_ID')]//preceding-sibling::td[contains(text(),'NUM_PROD')]//preceding-sibling::td[contains(text(),'TIPO_PROD')]//preceding-sibling::td[contains(text(),'AGRARIO')]//preceding-sibling::td//input\r\n"
	 * + "";
	 */

	String[] arrTipoProdNoFic = { "AHORRO", "CORRIENTE", "EXPRES" };

//=======================================================================================================================	
	public PageDestino(BasePageWeb parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser destinos.
	 */
	public String seleccionarDestino(String tipoProducto, String numeroProducto) throws Exception {
		this.waitPantallaDestino();
//-----------------------------------------------------------------------------------------------------------------------		
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "corriente"; // VALOR POR DEFECTO
		if (tipoProdUpper.contains("AHORRO") || tipoProdUpper.contains("ahorro"))
			tipoProd = "ahorro";
		else if (tipoProdUpper.contains("EXPRES") || tipoProdUpper.contains("expres")) // CREDIEXPRESS
			tipoProd = "rediexpress";
		else if (tipoProdUpper.contains("ELECTR") || tipoProdUpper.contains("electr")) // DEP�SITOS ELECTR�NICOS
			tipoProd = "electronicos";
		else if (tipoProdUpper.contains("DINERS") || tipoProdUpper.contains("diners"))
			tipoProd = "Tarjeta diners empresarial";
		else if (tipoProdUpper.contains("CREDIEXPRESS FIJO") || tipoProdUpper.contains("crediexpress fijo"))
			tipoProd = "CREDIEXPRESS FIJO";
		else if (tipoProdUpper.contains("CRÉDITO CORPORATIVO") || tipoProdUpper.contains("crédito corporativo")
				|| tipoProdUpper.contains("CREDITO CORPORATIVO") || tipoProdUpper.contains("credito corporativo"))
			tipoProd = "CRÉDITO CORPORATIVO";
		else if (tipoProdUpper.contains("LEASING FINANCIERO") || tipoProdUpper.contains("leasing financiero")) // DEP�SITOS
																												// ELECTR�NICOS
			tipoProd = "LEASING FINANCIERO";

		WebElement objRadioButtonProd = null;
		String xPathRB = xPathRBDestino.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",
				tipoProd);
		if (!tipoProdUpper.contains("FIC") || tipoProdUpper.contains("fic"))// NO ES FIC
			objRadioButtonProd = this.element(xPathRB);

		else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE,
				// CR�DIPLUS, CREDIEXPRESS
			xPathRB = xPathRBDestinoFic.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
			String tipoProdTab;
			List<WebElement> listaObjs = this.findElements(By.xpath(xPathRB));
			for (WebElement element : listaObjs) {
				tipoProdTab = this.getText(this.element(element, locTipoPruebaFic));
				if (!DXCUtil.itemContainsAnyArrayItem(tipoProdTab.toUpperCase(), arrTipoProdNoFic)) {
					objRadioButtonProd = element;
					break; // PARA TERMINAR EL CICLO
				}
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdDestino", this);
			return "Producto destino [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("ProductoDestino", this);
		this.click(locButtonSig);
		return null;
	}

	// ***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser destinos.
	 */
	public String seleccionarDestinoPropios(String tipoProducto, String numeroProducto) throws Exception {
		this.waitPantallaDestino();
		// -----------------------------------------------------------------------------------------------------------------------
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "Tarjetas cafetera";
		if (tipoProdUpper.contains("AGROPECUARIA") || tipoProdUpper.contains("agropecuaria")
				|| tipoProdUpper.contains("aGROPECUARIA"))
			tipoProd = "Tarjetas agropecuaria";
		else if (tipoProdUpper.contains("DINERS") || tipoProdUpper.contains("diners"))
			tipoProd = "Tarjeta diners empresarial";
		else if (tipoProdUpper.contains("Tarjeta Visa Signature") || tipoProdUpper.contains("tarjeta visa signature")
				|| tipoProdUpper.contains("Tarjeta visa signature") || tipoProdUpper.contains("TARJETA VISA SIGNATURE"))
			tipoProd = "Tarjeta visa signature";
		else if (tipoProdUpper.contains("Clásica Mastercard") || tipoProdUpper.contains("clásica mastercard")
				|| tipoProdUpper.contains("Clasica Mastercard") || tipoProdUpper.contains("Clásica mastercard")
				|| tipoProdUpper.contains("clasica mastercard") || tipoProdUpper.contains("CLÁSICA MASTERCARD"))
			tipoProd = "Clásica mastercard";
		else if (tipoProdUpper.contains("EMPRESARIAL MASTERCARD") || tipoProdUpper.contains("Empresarial mastercard"))
			tipoProd = "Empresarial mastercard";
		else if (tipoProdUpper.contains("EMPRESARIAL VISA") || tipoProdUpper.contains("Empresarial visa"))
			tipoProd = "Empresarial visa";
		else if (tipoProdUpper.contains("FAMILIARES") || tipoProdUpper.contains("familiares"))
			tipoProd = "Tarjeta crédito sociedades familiares";
		else if (tipoProdUpper.contains("GARANTIZADA PLATINUM VISA")
				|| tipoProdUpper.contains("garantizada platinum visa")
				|| tipoProdUpper.contains("Garantizada platinum visa"))
			tipoProd = "Garantizada platinum visa";
		else if (tipoProdUpper.contains("Leasing Financiero") || tipoProdUpper.contains("LEASING FINANCIERO"))
			tipoProd = "Leasing Financiero";
		else if (tipoProdUpper.contains("Leasing habitacional") || tipoProdUpper.contains("LEASING HABITACIONAL"))
			tipoProd = "Leasing habitacional";
		else if (tipoProdUpper.contains("CREDIEXPRESS FIJO") || tipoProdUpper.contains("crediexpress fijo"))
			tipoProd = "Crediexpress fijo";
		else if (tipoProdUpper.contains("CRÉDITO CORPORATIVO") || tipoProdUpper.contains("crédito corporativo")
				|| tipoProdUpper.contains("CREDITO CORPORATIVO") || tipoProdUpper.contains("credito corporativo"))
			tipoProd = "Crédito corporativo";
		else if (tipoProdUpper.equals("CRÉDITO") || tipoProdUpper.equals("Crédito"))
			tipoProd = "Crédito";
		else if (tipoProdUpper.equals("Cartera Ordinaria")||tipoProdUpper.equals("CARTERA ORDINARIA"))
			tipoProd = "Cartera Ordinaria";

		WebElement objRadioButtonProd = null;
		String xPathRB = xPathRBDestino.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",
				tipoProd);
		if (!tipoProdUpper.contains("FIC") || tipoProdUpper.contains("FIC"))// NO ES FIC
			objRadioButtonProd = this.element(xPathRB);

		else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE,
				// CR�DIPLUS, CREDIEXPRESS
			xPathRB = xPathRBDestinoFic.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
			String tipoProdTab;
			List<WebElement> listaObjs = this.findElements(By.xpath(xPathRB));
			for (WebElement element : listaObjs) {
				tipoProdTab = this.getText(this.element(element, locTipoPruebaFic));
				if (!DXCUtil.itemContainsAnyArrayItem(tipoProdTab.toUpperCase(), arrTipoProdNoFic)) {
					objRadioButtonProd = element;
					break; // PARA TERMINAR EL CICLO
				}
			}
		}
		// -----------------------------------------------------------------------------------------------------------------------
		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdDestino", this);
			return "Producto destino [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("ProductoDestino", this);
		this.click(locButtonSig);
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser destinos.
	 */
	public String seleccionarCuentaInscri(String tipoProducto, String numeroProducto) throws Exception {
		this.waitPantallaDestino();
		// -----------------------------------------------------------------------------------------------------------------------
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "corriente"; // VALOR POR DEFECTO
		if (tipoProdUpper.contains("ahorro otros bancos") || tipoProdUpper.contains("AHORRO OTROS BANCOS"))
			tipoProd = "Cuentas ahorro otros bancos";
		else if (tipoProdUpper.contains("AHORRO") || tipoProdUpper.contains("ahorro"))
			tipoProd = "ahorro";
		else if (tipoProdUpper.contains("corrientes otros bancos") || tipoProdUpper.contains("CORRIENTES OTROS BANCOS"))
			tipoProd = "Cuentas corrientes otros bancos";
		else if (tipoProdUpper.contains("EXPRES") || tipoProdUpper.contains("expres")) // CREDIEXPRESS
			tipoProd = "rediexpress";
		else if (tipoProdUpper.contains("ELECTR") || tipoProdUpper.contains("electr")) // DEP�SITOS ELECTR�NICOS
			tipoProd = "electronicos";

		WebElement objRadioButtonProd = null;
		do {
			DXCUtil.wait(3);
			String xPathRB = xPathRBDestino.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",
					tipoProd);
			if (!tipoProdUpper.contains("FIC") || tipoProdUpper.contains("FIC")) { // NO ES FIC
				objRadioButtonProd = this.element(xPathRB);
			} else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE,
						// CR�DIPLUS, CREDIEXPRESS
				xPathRB = xPathRBDestinoFic.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
				String tipoProdTab;
				List<WebElement> listaObjs = this.findElements(By.xpath(xPathRB));
				for (WebElement element : listaObjs) {
					tipoProdTab = this.getText(this.element(element, locTipoPruebaFic));
					if (!DXCUtil.itemContainsAnyArrayItem(tipoProdTab.toUpperCase(), arrTipoProdNoFic)) {
						objRadioButtonProd = element;
						break; // PARA TERMINAR EL CICLO
					}
				}
			}

			if (objRadioButtonProd != null) {
				// Se encontr� el producto, salir del bucle
				break;
			}
			DXCUtil.wait(6);
			// Buscar el bot�n de siguiente y verificar si est� habilitado
			WebElement btnSiguiente = this.element(By.xpath("//*[@id='cphCuerpo_lnkSiguiente']"));
			if (btnSiguiente != null && !btnSiguiente.getAttribute("class").contains("aspNetDisabled")) {
				DXCUtil.wait(6);
				this.element(btnSiguiente).click();
				// Realizar la b�squeda nuevamente despu�s de hacer clic en el bot�n de
				// siguiente
				objRadioButtonProd = this.element(xPathRB);
			} else {
				// Si no hay bot�n de siguiente o est� deshabilitado, salir del bucle
				break;
			}
		} while (true);

		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdDestino", this);
			return "Producto destino [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("selec Cuenta Inscri", this);
		this.click(locButtonSig);
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Este m�todo se usa para seleccionar una cuenta destino, que primero permite
	 * realizar un filtro de b�squeda.<br>
	 * El filtro se hace por el banco y por el n�mero de la cuenta.<br>
	 * Se sugiere usar para destinos Cuenta Inscrita.<br>
	 */
	public String seleccionarDestino(String bancDest, String tipoCuenta, String ctaDestin) throws Exception {

		String msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgAlerta != null) {
			Evidence.saveFullPage("SinCtasInscritas", this);
			return msgAlerta;
		}

		this.waitPantallaDestino();

		this.write(locNumdesctains, ctaDestin);

		String msgError = this.seleOption(locbandesctains, bancDest);
		if (msgError.isEmpty())
			return "Error en lista 'Banco Destino' : " + msgError;

		this.click(locBtnbuscar);
		DXCUtil.wait(2);
		// TERMINAR SELECCION DE CUENTA DESTINO PARA PAGOS CUENTA INSCRITA
		return this.seleccionarDestino(tipoCuenta, ctaDestin);
	}

//***********************************************************************************************************************
	public String ingresarDestino(String bancoDest, String tipoCta, String ctaDestino, String nombre, String tipoId,
			String numId) throws Exception {
		String msgError = null;
		this.waitPantallaDestino();
//------------------------------------------------------------------------------------------------------------------------
		String tipoProdUpper = tipoCta.toUpperCase();
		String tipoProd = "";

		if (tipoProdUpper.contains("AHORRO OTROS BANCOS") || tipoProdUpper.contains("Ahorro Otros Bancos")
				|| tipoProdUpper.contains("ahorro otros bancos")) {
			tipoProd = "CUENTAS AHORRO OTROS BANCOS";
		} else if (tipoProdUpper.contains("CORRIENTES OTROS BANCOS")
				|| tipoProdUpper.contains("Corrientes Otros Bancos")
				|| tipoProdUpper.contains("corriente otros bancos")) {
			tipoProd = "CUENTAS CORRIENTES OTROS BANCOS";
		} else if (tipoProdUpper.contains("Corriente") || tipoProdUpper.contains("corriente")
				|| tipoProdUpper.contains("CORRIENTE")) {
			tipoProd = "CUENTA CORRIENTE";
		} else if (tipoProdUpper.contains("Ahorros") || tipoProdUpper.contains("ahorros")
				|| tipoProdUpper.contains("AHORROS")) {
			tipoProd = "CUENTA AHORROS";
		} else if (tipoProdUpper.contains("DEPÓSITOS ELECTRONICOS") || tipoProdUpper.contains("DEPÓSITOS ELECTRÓNICOS")
				|| tipoProdUpper.contains("DEPOSITOS ELECTRONICOS") || tipoProdUpper.contains("Depésitos electronicos")
				|| tipoProdUpper.contains("Depositos electronicos")) {
			tipoProd = "DEPÓSITOS ELECTRONICOS";

		}
		do {
			DXCUtil.wait(3);
			msgError = this.seleOption(locBandest, bancoDest.toUpperCase());
		} while (!this.isDisplayed(locBandest));

//------------------------------------------------------------------------------------------------------------------------

		if (msgError.isEmpty()) {
			Evidence.saveFullPage("Error", this);
			return "Error en lista 'Banco Destino' : " + msgError;
		}

		do {
			DXCUtil.wait(3);
			msgError = this.seleOptionIgual(locTipctadest, tipoProd);
			if (msgError.isEmpty()) {
				if (tipoProd.equals("CUENTA AHORROS"))
					tipoProd = "CUENTA DE AHORROS";
				msgError = this.seleOptionIgual(locTipctadest, tipoProd);
			}
		} while (!this.isDisplayed(locTipctadest));

//------------------------------------------------------------------------------------------------------------------------

		if (msgError.isEmpty()) {
			Evidence.saveFullPage("Error", this);
			return "Error en lista 'Tipo Destino' " + tipoProd + " No encontrado: " + msgError;
		}

		DXCUtil.wait(4);
		this.write(locNumdest, ctaDestino);

		DXCUtil.wait(4);
		this.write(locNumid, numId);

		DXCUtil.wait(4);
		this.write(locNomtitu, nombre.toUpperCase());

//------------------------------------------------------------------------------------------------------------------------
		do {
			DXCUtil.wait(1);
			msgError = this.seleOptionIgual(locTipid, tipoId.toUpperCase());
		} while (!this.isDisplayed(locTipid));

		if (msgError.isEmpty())
			return "Error en lista 'Tipo de Identificación' : " + msgError;

		Evidence.saveFullPage("ProductoDestino", this);

		DXCUtil.wait(1);
		this.click(locButtonSig);

		DXCUtil.wait(5);
		String msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");

		if (msgAlerta != null) {

			return msgAlerta;
		}
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Ingresa la referencia del pago de una factura.
	 */
	public String ingresarReferencia(String referencia1, String referencia2) throws Exception {
		int contador = 0;
		this.waitPantallaDestino();
		String msgError = "";

		WebElement objReferencia = this.element(locReferencia1);
		if (objReferencia != null) {
			DXCUtil.wait(2);
			this.write(objReferencia, referencia1);
			msgError += "[REF1:" + referencia1 + "] - ";
		}

		objReferencia = this.element(locReferencia2);
		if (objReferencia != null) {
			DXCUtil.wait(2);
			this.write(objReferencia, referencia2);
			msgError += "[REF2:referencia2] - ";
		}
		Evidence.saveAllScreens("RefServicio", this);
		do {
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOu - No se encontro el boton: Siguiente");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(locButtonSig) == null);
		DXCUtil.wait(4);
		this.click(locButtonSig);
//-----------------------------------------------------------------------------------------------------------------------
		WebElement elementPaso;
		String msgAlerta;
		do { // ESPERA A QUE MUESTRE LA PANTALLA DE VALORES O UN ALERTA
			elementPaso = this.element(locPasoPageValor);
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (elementPaso == null && msgAlerta == null);
		if (msgAlerta != null) {
			Evidence.saveAllScreens("ErrorReferencia", this);
			return msgError + msgAlerta;
		}
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Ingresa la referencia del pago de una factura Pagos Automaticos.
	 */
	public String ingresarReferenciaAutom(String referencia1, String referencia2, String nombreArecordar)
			throws Exception {

		this.waitPantallaDestino();
		String msgError = "";

		WebElement objReferencia = this.element(locReferencia1Auto);
		if (objReferencia != null) {
			this.write(objReferencia, referencia1);
			msgError += "[REF1:" + referencia1 + "] - ";
		}

		WebElement objReferencia2 = this.element(locReferencia2Auto);
		if (objReferencia2 != null) {
			this.write(objReferencia2, referencia2);
			msgError += "[REF2:referencia2] - ";
		}

		// Esta validacion solo se hace cuando aparece los 3 objetos ya que los locators
		// se intercala con referencia 2
		WebElement objNombreArecordar = this.element(locNombreArecordar);
		if (objNombreArecordar != null) {
			if (nombreArecordar == null || nombreArecordar.isEmpty()) {	
				 nombreArecordar = "PagosAutomaticos" + SettingsRun.getCurrentIteration();
			}
			this.write(objNombreArecordar, nombreArecordar);
			msgError += "[nombre Titular] - ";
		}

		Evidence.saveAllScreens("RefServicio", this);
		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOu - No se encontro el boton: Siguiente");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(locButtonSig) == null);

		this.click(locButtonSig);
//-----------------------------------------------------------------------------------------------------------------------
		WebElement elementPaso;
		String msgAlerta;
		do { // ESPERA A QUE MUESTRE LA PANTALLA DE VALORES O UN ALERTA
			elementPaso = this.element(locPasoPageValor);
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (elementPaso == null && msgAlerta == null);
		if (msgAlerta != null) {
			Evidence.saveAllScreens("ErrorReferencia", this);
			return msgError + msgAlerta;
		}
		return null;
	}
//***********************************************************************************************************************

	public String adicionarDestino(String tipoId, String numId, String nombre, String apellido, String bancoDest,
			String tipoCtades, String numProdes, String valorPago, String referencia) throws Exception {

		String tipoCta = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();

		String nombre_Titular = nombre + "" + apellido;
		this.waitPantallaDestino();
		DXCUtil.wait(3);

		String msgError;

		if (tipoCta.contains("ompanita") || tipoCta.contains("emp pyme 22")) {

			this.write(locValorPagoNP, valorPago);

			this.write(locReferNP, referencia);

			msgError = this.seleOptionIgual(locTipCtaDestNP, tipoCtades);
			if (msgError.isEmpty())
				return "Error en lista 'Tipo Destino' : " + msgError;

		} else {

			msgError = this.seleOptionIgual(locTipIdNP, tipoId);
			
			if (msgError.isEmpty())
				return "Error en lista 'Tipo de Identificación' : " + msgError;

			this.write(locNumIdNP, numId);

			this.write(locNomtitu, nombre_Titular.toUpperCase());

			this.write(locValorPagoNP, valorPago);

			this.write(locReferNP, referencia);

			this.write(locNumDestNP, numProdes);

			this.selectListItem(locBanDestNP, bancoDest);

			if (tipoCtades.contains("DEPÓSITOS ELECTRÓNICOS")) {
				tipoCtades = "DEPÓSITOS ELECTRONICOS";
			}

			msgError = this.seleOptionIgual(locTipCtaDestNP, tipoCtades);
			if (msgError.isEmpty())
				return "Error en lista 'Tipo Destino' : " + msgError;

		}

		// NO SALVA EVIDENCIA PORQUE COMO ES PAGO MASIVO, SE SALVA LA EVIDENCIA CUANDO
		// VAYA A SEGUIR CON EL PAGO
		this.clickButton("Adicionar Cuenta");
		return null;
	}

//***********************************************************************************************************************

	public String adicionarDestinoAFC(String numeroAFC, String titular, String numId, String valorAporte,
			String valorCuotaCont) throws Exception {

		this.waitPantallaDestino();

		this.write(desnumeroCuenta, numeroAFC);

		this.write(destTitular, titular.toUpperCase());

		this.write(destnuId, numId);

		this.write(destValPago, valorAporte);

		this.write(destValContin, valorCuotaCont);

		// NO SALVA EVIDENCIA PORQUE COMO ES PAGO MASIVO, SE SALVA LA EVIDENCIA CUANDO
		// VAYA A SEGUIR CON EL PAGO
		this.click(adicionarCuenta);
		return null;
	}

	public String adicionarDestinoCred3ros(String tipoProdDestino, String numProdDestino, String titular,
			String numDocumento, String valorPagar) throws Exception {

		this.waitPantallaDestino();

		String msgError = this.seleOptionIgual(lisdestinoTipoProducto, tipoProdDestino);
		if (msgError.isEmpty())
			return "Error en lista 'Tipo de producto a pagar' : " + msgError;

		this.write(campDesNumeroCuenta, numProdDestino);

		this.write(campDestTitular, titular);
		this.write(campDestIdentificacion, numDocumento);
		this.write(campDestinoValorPago, valorPagar);

		// NO SALVA EVIDENCIA PORQUE COMO ES PAGO MASIVO, SE SALVA LA EVIDENCIA CUANDO
		// VAYA A SEGUIR CON EL PAGO
		this.clickButton("Adicionar producto");
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Garantiza que la pantalla quede en "Valores" cuando no se ha presentado
	 * error.
	 */
	public String cargarDestinosXArchivo(String formato, String nbArchivoCargar) throws Exception {
		Evidence.save("PantallaDestino");
		this.waitPantallaDestino();

		String formatoArchivo = "Archivo Plano";
		if (formato.equals("EXCEL"))
			formatoArchivo = "Excel";
		String msgLis = this.selectListItem(locFormatoArchivo, formatoArchivo);
		if (!msgLis.isEmpty()) {
//			Evidence.save(msgLis, this);
			Evidence.save(msgLis);
			return "Seleccionando : " + msgLis;
		}
		this.write(locFileCarga, nbArchivoCargar);
		this.clickButton("Cargar Archivo");

		String msgAlerta;
		do { // ESPERA A QUE MUESTRE ALERTA RESPECTIVA
			DXCUtil.wait(1);
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		} while (msgAlerta == null);
//-----------------------------------------------------------------------------------------------------------------------
		if (msgAlerta.contains("El archivo no ha podido cargarse")) {
			String idWindActual = this.getIdWindow();
			if (this.element(locLinkError) != null) 

				this.click(locLinkError);
			
			if (this.element(locLinkErrorclick) != null) 
				this.click(locLinkErrorclick);
			

			List<String> listWindows;
			do {
				listWindows = this.getIdWindows();
			} while (listWindows.size() == 1);
			this.changeWindow(listWindows.get(1));
			Evidence.saveAllScreens("ErrorCarga", this);
			this.closeCurrentBrowser();
			this.changeWindow(idWindActual);
			return "Error en la carga del archivo";
		}
//-----------------------------------------------------------------------------------------------------------------------
		// SI LLEGA A ESTE PUNTO PUDO CARGAR EL ARCHIVO
		return this.verificarContinuarCargaMasiva();
	}

//***********************************************************************************************************************
	public String verificarContinuarCargaMasiva() throws Exception {
		// SI LLEGA A ESTE PUNTO PUDO CARGAR EL ARCHIVO
		Evidence.saveFullPage("ProdDestinos", this);
		DXCUtil.wait(4);
		this.clickButton("Verificar y Continuar");
//-----------------------------------------------------------------------------------------------------------------------
		String msgAlerta;
		WebElement elementPaso, conerror;
		int contador = 1;
		do { // ESPERA A QUE MUESTRE LA PANTALLA DE VALORES O ALERTA
			DXCUtil.wait(1);
			contador++;
			conerror = this.element(bntContinuarConErr);
			elementPaso = this.element(locPasoPageValor);
			msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
			if (contador >= 60) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				if (conerror == null) {
					if (msgAlerta != null) 						
					Reporter.reportEvent(Reporter.MIC_FAIL, msgAlerta);
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el botón: Verificar y Continuar");
				}
				this.terminarIteracion();
			}
		} while (elementPaso == null && msgAlerta == null && conerror == null);
		
		if (msgAlerta != null &&!msgAlerta.isEmpty()) 
		Reporter.reportEvent(Reporter.MIC_INFO, msgAlerta);
		
		if (elementPaso == null && msgAlerta != null || conerror != null|| msgAlerta.contains("El archivo se cargó exitosamente")) { // HAY ERROR DE VERIFICACI�N
			Evidence.saveAllScreens("ErrorVerificando", this);
			DXCUtil.wait(2);
			if (msgAlerta != null) {
			if (msgAlerta.equals(" ")|| msgAlerta.equals("")||msgAlerta.contains("errores")) {
				WebElement verifi = null;
				do {
					DXCUtil.wait(1);
					contador++;
					 verifi = this.element(veri);
					if (contador > 30) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
						if (conerror == null) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msgAlerta);
							Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el botón: Continuar con errores");
						}

						this.terminarIteracion();
					}
				} while (verifi == null);
				
				
				this.clickButton("Continuar con errores");
			}
		}else {
			WebElement verifi = null;
			do {
				DXCUtil.wait(1);
				contador++;
				verifi = this.element(veri);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
					if (conerror == null) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el botón: Continuar con errores");
					}

					this.terminarIteracion();
				}
			} while (verifi == null);
			this.clickButton("Continuar con errores");
		}

		}
		return null;
	}

//***********************************************************************************************************************
	public void waitPantallaDestino() throws Exception {
		WebElement elementPaso;
		int contador = 0;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCI�N DEL DESTINO
			DXCUtil.wait(1);
			contador++;
			elementPaso = this.element(locPasoPage);
			if (contador > 60) {
				Evidence.save("PantallaDestino");
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				this.terminarIteracion();
			}
		} while (elementPaso == null);
		Evidence.save("PantallaDestino");
	}
//***********************************************************************************************************************
}
