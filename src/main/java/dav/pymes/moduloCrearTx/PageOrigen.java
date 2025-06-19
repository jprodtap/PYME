package dav.pymes.moduloCrearTx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dav.Consultas_Y_Extractos.PageConsultasyExtractos;
import dav.pymes.PagePortalPymes;
import dav.transversal.DatosDavivienda;
import dav.transversal.MovimientoStratus;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageOrigen extends PagePortalPymes {

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Origen']");
	By locTitTabOr = By.xpath("//td[@class='TituloRojo'][text()='Producto Origen de los Fondos:']");
	By locTitTabOrAut = By.xpath("//td[@class='TituloRojo'][text()='Producto origen de los fondos:   ']");

	String xPathLnkServicio = "//a[text()='NB_SERVICIO']";
	String xPathRBOrigen = "//td[contains(text(),'NUM_PROD')]//preceding-sibling::td[contains(text(),'TIPO_PROD')]//preceding-sibling::td//input";
	String xPathRBOrigenAuto = "(//span[contains(text(),'NUM_PROD')]/../../td[contains(text(),'TIPO_PROD')]/preceding-sibling::td//input)[POS]";
	String xPathRBOrigenFic = "//td[contains(text(),'NUM_PROD')]//preceding-sibling::td/input";
	String xPathRBOrigenFicAuto = "//span[contains(text(),'NUM_PROD')]//preceding-sibling::td/input";
	String xPathServicio = "//select[@id='cphCuerpo_dropEmpresaRecaudo']";
	String xPathServicioAutomaticos = "//select[@id='cphCuerpo_ddConvenioRecaudos']";
	String xPathLinkNumAprobacion = "//*[@id=\"cphCuerpo_gvTransacciones\"]/tbody/tr/td[2]/a[contains(text(), 'NUM_APROBACION')]";
	String xPathLinkNomProv = "//*[@id=\"cphCuerpo_gvTransaccionesMasivas\"]/tbody/tr[NUM_REGISTRO]/td[6]/a";
	String xPathDataNomProv = "//*[@id=\"cphCuerpo_gvTransaccionesMasivas\"]/tbody/tr[NUM_REGISTRO]/td[4]";

	By locTipoPruebaFic = By.xpath("./parent::td/following-sibling::td[1]");
	By locButtonSig = By.xpath("//input[contains(@value, 'Siguiente')]");
	By btnConsultasExtractos = By.xpath("//*[@id=\"lblMasterAlerta\"]/a[1]");
	By btnDatosAdenda = By.xpath("//*[@id=\"cphCuerpo_btnMostrarDatos\"]");
	By tableAdenda = By.xpath("//*[@id=\"cphCuerpo_gvMovimientosCuentas\"]");
	By tableTxAdenda = By.xpath("//*[@id=\"cphCuerpo_gvTransacciones\"]");

	// *[@id="formPincipal:tablaResultadoConsultaProcesos"]/tbody/tr[1]/td[contains(text(),'Cris')]

	String xpathNumEstadoPago = "(//*[@id=\"formPincipal:tablaResultadoConsultaPagos\"]/tbody/tr/td[2][contains(text(),'NUM_PROCESO_PAGO')])";
	String xpathNumProcesoPago = "//*[@id=\"formPincipal:tablaResultadoConsultaProcesos\"]/tbody/tr[NUM]/td[1][contains(text(),'NUM_PROCESO_PAGO')]";
	By btnBuscarEstadoProceso = By.xpath("//*[@id=\"formPincipal:buscar\"]");

//=======================================================================================================================	
	// Locator Recibir del exterior Origen
	By iframeInternacionales = By.id("cphCuerpo_IframeDivisas");
	By iframeInternacionales1 = By.id("cphCuerpo_IframeDivisas (Paso1)");
	By campModRecibir = By.xpath("//li[@onclick][2]");
	By cmpPopup = By.xpath("//div[@id='AlertaModal']//h4//strong");
	By btnPopup = By.xpath("//*[@id='btnmodal']");
	By titMo = By.xpath("//h3[text()='Transferencias internacionales']");
	By tbOrigen = By.xpath("//table[@class='table  table-responsive table-condensed table-hover table-bordered mt10']");

	String numRefTxIntxpath = "//tbody/tr[td[contains(text(), 'MONEDA_INTERNACIONAL')] and td[contains(text(), 'NUM_REF_RECI_INTERNACIONAL')]]/th/input[@type='radio']";
	String cuentaDesTxIntxpath = "//tbody/tr[td[contains(text(), 'NUM_PROD')] and td[contains(text(), 'TIPO_PROD')]]/th/input[@type='radio']";
	By seleConceptoCambiario = By.xpath("//select[@id='ConceptoCambiario']");
	By seleNumCambiario = By.xpath("//select[@id='Numerales_NumeralCambiario_0']");

	By campValorIn = By.xpath("//input[@id='Numerales_valor_0']");
	By ButtonSigIn = By.xpath("//button[@class='dm-boton-proceso dm-btn-fondo-rojo']");
	By tituMoAbono = By.xpath("//strong[text()='Cuenta para abono']");
	By tituMoCotizar = By.xpath("//strong[text()='Cotizar la transferencia']");
	By tituMoConfirmación = By.xpath("//strong[text()='Recibir desde el exterior']");

	String saldoDisXpath = "//td[contains(text(),'NUMCUENTA')]//preceding-sibling::td[contains(text(),'TIPOCUENTA')]/following-sibling::td/span";
	String saldoDisXpathMismoNit = "//td[contains(text(),'NUMCUENTA')]//preceding-sibling::td[contains(text(),'TIPOCUENTA')]//following::td[6]";
	String saldoDisServiciosXpath = "//td[contains(text(),'TIPOCUENTA')]//preceding::td[contains(text(),'NUMCUENTA')]/following-sibling::td[1]";
	String saldoDisServiciosXpath2 = "//tr[td[contains(text(),'TIPOCUENTA')] and td[contains(text(),'NUMCUENTA')]]//td[NUM]";

	String[] arrTipoProdNoFic = { "AHORRO", "CORRIENTE", "DIPLUS", "EXPRES" };

	static String saldoTotalInicial = null;
	static String saldoDispoInicial = null;
	static String saldoTotalFinal = null;
	static String saldoDispoFinal = null;

//=======================================================================================================================	
	public PageOrigen(BasePageWeb parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Retorna [null] si se pudo ir al link del servicio, en caso contrario retorna
	 * un mensaje de error.
	 */
	public String irAServicio(String servicio) throws Exception {
		String nbLink = this.getNbLinkServicio(servicio);
		WebElement objLinkServ = this.element(xPathLnkServicio.replace("NB_SERVICIO", nbLink));
		if (objLinkServ == null) {
			Evidence.saveAllScreens("ErrorNoServicio", this);
			return "No se encuentra el link para el servicio [" + servicio + "] -- Se buscó [" + nbLink + "]";
		}
		// DA CLICK EN EL LINK DEL SERVICIO
		this.click(objLinkServ);
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser origen de los
	 * fondos.<br>
	 * Retorna [null] si se pudo hacer la selección, en caso contrario retorna un
	 * mensaje de error, si pudo hacer la selección da click en el "Continuar".
	 */
	public String seleccionarOrigen(String servicio,String tipoDocumento,String numeroDoc,String tipoProducto, String numeroProducto) throws Exception {
		// 2
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(locPasoPage);
//			DXCUtil.Movercursor();
		} while (elementPaso == null);
		Evidence.saveFullPage("Origen", this);
//-----------------------------------------------------------------------------------------------------------------------		
		String tipoProdUpper = tipoProducto;
		String tipoProd = " "; // VALOR POR DEFECTO

		if (tipoProdUpper.contains("AHORROS") || tipoProdUpper.contains("ahorros"))
			tipoProd = "ahorro";
		else if (tipoProdUpper.contains("CORRIENTE") || tipoProdUpper.contains("corriente")) // CRÉDIPLUS
			tipoProd = "corriente";
		else if (tipoProdUpper.contains("DIPLUS") || tipoProdUpper.contains("diplus")) // CRÉDIPLUS
			tipoProd = "diplus";
		else if (tipoProdUpper.contains("EXPRES") || tipoProdUpper.contains("rediexpress")) { // CREDIEXPRESS

			if (tipoProdUpper.contains("rediexpress") && tipoProdUpper.contains("ompanita")) {
				tipoProd = "ompanita";

			} else if (tipoProdUpper.contains("rediexpress") && tipoProdUpper.contains("emp pyme 22")) { // CREDIEXPRESS
				tipoProd = "emp pyme 22";

			} else {
				tipoProd = "rediexpress";
			}
		} else if (tipoProdUpper.contains("Agropecuaria"))
			tipoProd = "Tarjetas Agropecuaria";
		else if (tipoProdUpper.contains("agropecuaria"))
			tipoProd = "Tarjetas agropecuaria";
		else if (tipoProdUpper.contains("DINERS"))
			tipoProd = "Tarjeta Diners Empresarial";
		else if (tipoProdUpper.contains("Clásica Mastercard") || tipoProdUpper.contains("clásica mastercard")
				|| tipoProdUpper.contains("Clasica Mastercard") || tipoProdUpper.contains("clasica mastercard"))
			tipoProd = "Clásica Mastercard";
		else if (tipoProdUpper.contains("MASTERCARD") || tipoProdUpper.contains("mastercard"))
			tipoProd = "Empresarial mastercard";
		else if (tipoProdUpper.contains("visa"))
			tipoProd = "Empresarial visa";
		else if (tipoProdUpper.contains("FAMILIARES") || tipoProdUpper.contains("familiares")
				|| tipoProdUpper.contains("Familiares"))
			tipoProd = "Tarjeta Credito Sociedades Familiares";
		else if (tipoProdUpper.contains("Garantizada Platinum Visa"))
			tipoProd = "Garantizada Platinum Visa";
		else if (tipoProdUpper.contains("Tarjeta Visa Signature	"))
			tipoProd = "Tarjeta Visa Signature";

		WebElement objRadioButtonProd = null;
		String xPathRB = xPathRBOrigen.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",
				tipoProd);
		if (!tipoProdUpper.contains("FIC"))// NO ES FIC
			objRadioButtonProd = this.element(xPathRB);
		else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE,
				// CRÉDIPLUS, CREDIEXPRESS
			xPathRB = xPathRBOrigenFic.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
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
			Evidence.saveFullPage("Error-ProdOrigen", this);
			return "Producto origen [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		if (!DatosDavivienda.IS_RISKMOTOR)
			this.validacionSaldosStratus(servicio,tipoDocumento,numeroDoc,tipoProd, numeroProducto, true);

		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		DXCUtil.wait(3);
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("ProductoOrigen", this);
		DXCUtil.wait(3);
		this.click(locButtonSig);
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Se presenta la tabla con los productos que pueden ser origen de los
	 * fondos.<br>
	 * Retorna [null] si se pudo hacer la selección, en caso contrario retorna un
	 * mensaje de error, si pudo hacer la selección da click en el "Continuar".
	 */
	public String seleccionarOrigenAutomatico(String tipoProducto, String numeroProducto, String posicion)
			throws Exception {
		//
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(locPasoPage);
//			DXCUtil.Movercursor();
		} while (elementPaso == null);
//-----------------------------------------------------------------------------------------------------------------------		
		String tipoProdUpper = tipoProducto;
		String tipoProd = "No se encontro Producto"; // VALOR POR DEFECTO

		if (tipoProdUpper.contains("AHORROS") || tipoProdUpper.contains("ahorros"))
			tipoProd = "ahorro";
		else if (tipoProdUpper.contains("CORRIENTE") || tipoProdUpper.contains("corriente")) // CRÉDIPLUS
			tipoProd = "corriente";
		else if (tipoProdUpper.contains("DIPLUS") || tipoProdUpper.contains("diplus")) // CRÉDIPLUS
			tipoProd = "diplus";
		else if (tipoProdUpper.contains("EXPRES") || tipoProdUpper.contains("rediexpress")) // CREDIEXPRESS
			tipoProd = "rediexpress";
		else if (tipoProdUpper.contains("Agropecuaria"))
			tipoProd = "Tarjetas Agropecuaria";
		else if (tipoProdUpper.contains("agropecuaria"))
			tipoProd = "Tarjetas agropecuaria";
		else if (tipoProdUpper.contains("DINERS"))
			tipoProd = "Tarjeta Diners Empresarial";
		else if (tipoProdUpper.contains("Clásica Mastercard") || tipoProdUpper.contains("clásica mastercard")
				|| tipoProdUpper.contains("Clasica Mastercard") || tipoProdUpper.contains("clasica mastercard"))
			tipoProd = "Clásica Mastercard";
		else if (tipoProdUpper.contains("MASTERCARD") || tipoProdUpper.contains("mastercard"))
			tipoProd = "Empresarial mastercard";
		else if (tipoProdUpper.contains("visa"))
			tipoProd = "Empresarial visa";
		else if (tipoProdUpper.contains("FAMILIARES") || tipoProdUpper.contains("familiares")
				|| tipoProdUpper.contains("Familiares"))
			tipoProd = "Tarjeta crédito sociedades familiares";
		else if (tipoProdUpper.contains("Garantizada Platinum Visa"))
			tipoProd = "Garantizada Platinum Visa";
		else if (tipoProdUpper.contains("Tarjeta Visa Signature	"))
			tipoProd = "Tarjeta Visa Signature";

		WebElement objRadioButtonProd = null;
		String xPathRB = xPathRBOrigenAuto.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4))
				.replace("TIPO_PROD", tipoProd).replace("POS", posicion);
		if (!tipoProdUpper.contains("FIC"))// NO ES FIC
			objRadioButtonProd = this.element(xPathRB);
		else { // ES FIC, PUEDE SER CUALQUIER TIPO DE PRODUCTO <> AHORROS, CORRIENTE,
				// CRÉDIPLUS, CREDIEXPRESS
			xPathRB = xPathRBOrigenFicAuto.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4));
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
			Evidence.saveFullPage("Error-ProdOrigen", this);
			return "Producto origen [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("ProductoOrigen" + posicion, this);
		return null;
	}

//***********************************************************************************************************************
	public String seleccionarOrigen(String servicio, String tipoDocumento,String numeroDoc,String numConvenio, String tipoProducto, String numeroProducto) throws Exception {
//1
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(locPasoPage);
		} while (elementPaso == null);
		String msgError = null;
//-----------------------------------------------------------------------------------------------------------------------		

		if (this.element(xPathServicio) != null) {
			String selecc = "- " + numConvenio; // EL CONVENIO SE MUETSRA CON UN PREFIJO "- "
			msgError = this.selectListContainsItems(By.xpath(xPathServicio), selecc);
			if (!DatosDavivienda.IS_RISKMOTOR && DatosDavivienda.STRATUS != null) {
				String[] arrayDatosConv = { "NUMERO ID", "NOMBRE CONV", "OF RADIC", "TIP REC" };
				String[] datosconvenio = DatosDavivienda.STRATUS.consultarConvenio(selecc, arrayDatosConv);
			}
		}

		if (msgError == null || !msgError.isEmpty())
			return "Error en lista 'Servicio a pagar' : " + msgError;

		WebElement objTablaOrigenes;
		int contador = 1;
		do { // ESPERA MIENTRAS NO SE PRESENTA LA TABLA DE ORÍGENES
			DXCUtil.wait(1);
			contador++;
//			DXCUtil.Movercursor();
			objTablaOrigenes = this.element(locTitTabOr);

		} while (objTablaOrigenes == null && contador > 240);

		msgError = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgError != null) {
			return "Error en lista 'Servicio a pagar' : " + msgError;
		}
		return this.seleccionarOrigen(servicio,tipoDocumento,numeroDoc,tipoProducto, numeroProducto);
	}

//***********************************************************************************************************************
	public String seleccionarOrigenAuto(String numConvenio, String tipoProducto, String numeroProducto,
			String tipoProductoSec, String numeroProductoSec) throws Exception {
//1
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(locPasoPage);
		} while (elementPaso == null);
		String msgError = null;
//-----------------------------------------------------------------------------------------------------------------------		
		if (this.element(xPathServicioAutomaticos) != null) {
			msgError = this.selectListContainsItems(By.xpath(xPathServicioAutomaticos), numConvenio);
		}

		if (msgError == null || !msgError.isEmpty())
			return "Error en lista 'Servicio a pagar' : " + msgError;

		WebElement objTablaOrigenes;
		int contador = 1;
		do { // ESPERA MIENTRAS NO SE PRESENTA LA TABLA DE ORÍGENES
			DXCUtil.wait(1);
			contador++;
			objTablaOrigenes = this.element(locTitTabOrAut);
			if (contador > 120) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				if (objTablaOrigenes == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se presento el Titulo");
				}
			}
		} while (objTablaOrigenes == null);

		msgError = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgError != null) {
			return "Error en lista 'Servicio a pagar' : " + msgError;
		}
		msgError = this.seleccionarOrigenAutomatico(tipoProducto, numeroProducto, "1");

		DXCUtil.wait(2);

		if (!tipoProductoSec.isEmpty() && !numeroProductoSec.isEmpty()) {
			msgError = this.seleccionarOrigenAutomatico(tipoProductoSec, numeroProductoSec, "2");

		}
		this.click(locButtonSig);
		return msgError;
	}

//***********************************************************************************************************************
	/**
	 * Retorna el nombre del link del sevicio tal como se presenta en el Portal.<br>
	 * Si el [servicio] no es uno d elos que se esperan, retorna "NO HAY LINK
	 * DISPONIBLE".
	 */
	private String getNbLinkServicio(String servicio) {

		String serv = servicio;
		String nbServicio = "NO HAY LINK DISPONIBLE";
		if (serv.contains("Nómi"))
			nbServicio = "Nómina";
		else if (serv.contains("Prove"))
			nbServicio = "Proveedores";
		else if (serv.contains("AFC"))
			nbServicio = "Cuentas AFC";
		else if (serv.contains("Serv") || serv.contains("Fact"))
			nbServicio = "Pago de factura";
		else if (serv.contains("Pagos Automaticos") || serv.contains("Automaticos"))
			nbServicio = "Programación pagos automáticos";
		else if (serv.contains("Prove"))
			nbServicio = "Proveedores";
		else if (serv.contains("NIT"))
			nbServicio = "Entre productos Davivienda del mismo NIT";
		else if (serv.contains("Cuenta No Inscrita") || serv.contains("Cuenta NO Inscrita"))
			nbServicio = "Hacia un tercero con cuenta NO inscrita";
		else if (serv.contains("Cuenta Inscrita"))
			nbServicio = "Hacia un tercero con cuenta inscrita";
		else if (serv.contains("Propios"))
			nbServicio = "Sus Productos Davivienda";
		else if (serv.contains("Avance"))
			nbServicio = "Avances Tarjeta de Crédito";
		else if (serv.contains("créditos") || serv.contains("terceros") || serv.contains("Crédito.3ros")
				|| serv.contains("Créditos") || serv.contains("Terceros"))
			nbServicio = "Créditos de terceros";
		else if (serv.contains("Internacionales") || serv.contains("internacionales")
				|| serv.contains("Divisas Documentos y Formularios"))
			nbServicio = "Transferencias Internacionales";
		else if (serv.contains("ORPA"))
			nbServicio = "Órdenes de Pago";

		return nbServicio;
	}
//***********************************************************************************************************************	

	public void TxInternacionalesOrigen() throws Exception {
		do {
			DXCUtil.wait(1);
		} while (!this.isDisplayed(iframeInternacionales));

		Evidence.saveAllScreens("Transferencias Int", this);
		WebElement iframe = this.findElement(By.id("cphCuerpo_IframeDivisas"));
		this.getDriver().switchTo().frame(iframe);
		// Encontrar y hacer clic en el enlace "Transacciones Internacionales"
		do {
			DXCUtil.wait(1);
		} while (!this.isDisplayed(campModRecibir));

		this.click(campModRecibir);
		this.closeActiveIntAlert();
//		this.aceptarPopUp();

		String numRef, moneda, concepTransferencia, valor = null;
		this.seleccionarOrigenInter("1707SWF584", "JPY");

		do {
			DXCUtil.wait(1);
		} while (!this.isDisplayed(seleConceptoCambiario) && !this.isEnabled(seleConceptoCambiario));

		this.selectListItem(seleConceptoCambiario, "5 - Servicios, transferencias y otros conceptos");

		DXCUtil.wait(6);

		concepTransferencia = "5 - Servicios, transferencias y otros conceptos";

		if (concepTransferencia.contains("2") || concepTransferencia.contains("3")) {
			do {
				DXCUtil.wait(1);
			} while (!this.isDisplayed(seleConceptoCambiario) && !this.isEnabled(seleConceptoCambiario));

			this.selectListItem(seleConceptoCambiario, "1712");

			valor = "10000";
			if (this.isDisplayed(campValorIn) && this.isEnabled(campValorIn) && valor.isEmpty()) {

			}

		} else if (concepTransferencia.contains("5") || concepTransferencia.contains("4")) {
			do {
				DXCUtil.wait(1);
			} while (!this.isDisplayed(seleNumCambiario) && !this.isEnabled(seleNumCambiario));

			this.seleOption(seleNumCambiario, "1070");
		}
		this.click(ButtonSigIn);
	}

//	public String aceptarPopUp() {
//		WebElement accept = null;
//		do {
//			accept = this.element(By.id("btnmodal"));
//		DXCUtil.wait(1);
//		}while(accept==null);
//		this.click(accept);
////		accept.click();
//		return "";
//}

//	private void closeActiveIntAlert() throws Exception {
//		int contador = 1;
//		do {
//			DXCUtil.wait(6);
//			contador++;
//		} while (!this.isDisplayed(cmpPopup) && !this.isDisplayed(btnPopup) && !this.isEnabled(btnPopup)
//				&& contador > 4);
//
//		if (this.isDisplayed(btnPopup) && this.isDisplayed(cmpPopup) && this.isEnabled(btnPopup)) {
//			Evidence.saveAllScreens("Alert", this);
//			this.click(btnPopup);
//		}
//	}

	// ***********************************************************************************************************************
	public String seleccionarOrigenInter(String numRef, String moneda) throws Exception {

		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
		} while (this.element(titMo) == null && this.element(tbOrigen) == null);
		// -----------------------------------------------------------------------------------------------------------------------
		Evidence.saveAllScreens("Recibir", this);
		WebElement objTablaOrigenes;
		int contador = 1;
		do { // ESPERA MIENTRAS NO SE PRESENTA LA TABLA DE ORÍGENES
			DXCUtil.wait(1);
			contador++;
			objTablaOrigenes = this.element(tbOrigen);
		} while (objTablaOrigenes == null && contador > 240);

		WebElement objRadioButtonProd = null;
		String xPathRB = numRefTxIntxpath.replace("NUM_REF_RECI_INTERNACIONAL", numRef).replace("MONEDA_INTERNACIONAL",
				moneda);
		objRadioButtonProd = this.findElement(By.xpath(xPathRB));
//-----------------------------------------------------------------------------------------------------------------------
		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdOrigen", this);
			return "Número de refencia Externa [" + numRef + " - " + moneda + "] NO encontrado";
		}

		do {
			DXCUtil.wait(1);
		} while (!objRadioButtonProd.isDisplayed() && !objRadioButtonProd.isEnabled());
		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);

		return null;
	}

	public String seleccionarCuenta(String tipoProducto, String numeroProducto) throws Exception {
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(tituMoAbono);
		} while (elementPaso == null);
//-----------------------------------------------------------------------------------------------------------------------		
		String tipoProdUpper = tipoProducto.toUpperCase();
		String tipoProd = "CORRIENTE"; // VALOR POR DEFECTO
		if (tipoProdUpper.contains("AHORROS"))
			tipoProd = "AHORROS";
		else if (tipoProdUpper.contains("DIPLUS")) // CRÉDIPLUS
			tipoProd = "diplus";
		else if (tipoProdUpper.contains("EXPRES")) // CREDIEXPRESS
			tipoProd = "rediexpress";
		else if (tipoProdUpper.contains("AGROPECUARIA"))
			tipoProd = "Tarjetas agropecuaria";
		else if (tipoProdUpper.contains("DINERS"))
			tipoProd = "Tarjeta Diners Empresarial";
		else if (tipoProdUpper.contains("MASTERCARD"))
			tipoProd = "Empresarial mastercard";
		else if (tipoProdUpper.contains("visa"))
			tipoProd = "Empresarial visa";
		else if (tipoProdUpper.contains("FAMILIARES") || tipoProdUpper.contains("Familiares"))
			tipoProd = "Tarjeta crédito sociedades familiares";

		WebElement objRadioButtonProd = null;
		String xPathRB = cuentaDesTxIntxpath.replace("NUM_PROD", DXCUtil.right(numeroProducto, 4)).replace("TIPO_PROD",
				tipoProd);

		objRadioButtonProd = this.element(xPathRB);

//-----------------------------------------------------------------------------------------------------------------------
		if (objRadioButtonProd == null) {
			Evidence.saveFullPage("Error-ProdOrigen", this);
			return "Producto  [" + tipoProducto + " - " + numeroProducto + "] NO encontrado";
		}

		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
		} while (!objRadioButtonProd.isDisplayed() && !objRadioButtonProd.isEnabled());

		// SI LLEGA A ESTE ES PORQUE EXISTE EL PRODUCTO ORIGEN DE LOS FONDOS
		this.click(objRadioButtonProd);
		Evidence.saveFullPage("Producto selecionado", this);
		DXCUtil.wait(4);
		this.click(ButtonSigIn);

		return null;
	}

// ***********************************************************************************************************************
	/**
	 * Selecciona la cuenta en un locator Select.
	 */
	public void SeleccionarOrigenDetinoAdenda(String xPathRBOrigen, String item) throws Exception {

		this.selectListItem(By.xpath(xPathRBOrigen), item);
	}

// ***********************************************************************************************************************
	/**
	 * Realiza el flujo de adenda para la primera ventana de "Consultas y
	 * Extractos"<br>
	 * <b> String es la descripcion de la transaccion que se esta ejecutando, como
	 * aparece en la tabla de "Consultas y Extractos"<br>
	 */
	public void NotaDebitoAdenda(String descripcion, String detalleVacio) throws Exception {

		if (descripcion.equals("Descuento por Pago a Proveedores")
				|| descripcion.equals("Descuento para pago de Nomina")) {

			String numRegistros = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();

			int resta = 0;
			int primerNumero = 0;
			int segundoNumero = 0;

			String[] parts = numRegistros.split(" ");

			primerNumero = Integer.parseInt(parts[0]);
			segundoNumero = Integer.parseInt(parts[2]);

			resta = segundoNumero - primerNumero;

			int i = 1;

			do {

				boolean encontrado = false;
				String registroAdenda = "(//*[@id=\"cphCuerpo_gvMovimientosCuentas\"]"
						+ "/tbody/tr[FILA]/td/span[contains(text(), 'DESCRIPCION')])[1]";

				String locDataAdenda;
				String DataAdenda;

				do {
				} while (this.element(btnDatosAdenda) == null);

				this.click(btnDatosAdenda);

				do {
				} while (this.element(tableAdenda) == null);

				Evidence.saveAllScreens("1", this);

				registroAdenda = registroAdenda.replace("FILA", "1").replace("DESCRIPCION", descripcion);

				if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
					registroAdenda = registroAdenda.replace("td", "td[4]")
							.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
					registroAdenda = registroAdenda.replace("tr[1]", "tr[" + (0 + i) + "]");
					locDataAdenda = registroAdenda.replace("td[4]", "td[3]");

					WebElement WebDataAdenda = this.findElement(By.xpath(locDataAdenda));
					String dataAdenda = WebDataAdenda.getText();

					Reporter.reportEvent(Reporter.MIC_PASS, ".........." + dataAdenda + "...........");

					this.click(this.element(registroAdenda));
					encontrado = true;
				}

				registroAdenda = registroAdenda.replace("tr[1]", "tr[2]").replace("DESCRIPCION", descripcion);

				if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
					registroAdenda = registroAdenda.replace("td", "td[4]")
							.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
					registroAdenda = registroAdenda.replace("tr[2]", "tr[" + (1 + i) + "]");
					locDataAdenda = registroAdenda.replace("td[4]", "td[3]").replace("/a", "");

					WebElement WebDataAdenda = this.findElement(By.xpath(locDataAdenda));
					String dataAdenda = WebDataAdenda.getText();

					Reporter.reportEvent(Reporter.MIC_PASS, ".........." + dataAdenda + "...........");

					this.click(this.element(registroAdenda));
					encontrado = true;
				}

				registroAdenda = registroAdenda.replace("tr[2]", "tr[3]").replace("DESCRIPCION", descripcion);

				if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
					registroAdenda = registroAdenda.replace("td", "td[4]")
							.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
					registroAdenda = registroAdenda.replace("tr[3]", "tr[" + (2 + i) + "]");
					locDataAdenda = registroAdenda.replace("td[4]", "td[3]");

					WebElement WebDataAdenda = this.findElement(By.xpath(locDataAdenda));
					String dataAdenda = WebDataAdenda.getText();

					Reporter.reportEvent(Reporter.MIC_PASS, ".........." + dataAdenda + "...........");

					this.click(this.element(registroAdenda));
					encontrado = true;
				}

				DXCUtil.wait(4);

				if (encontrado) {
					this.NotaDebitoDetalleReferencia(detalleVacio);

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el registro de la transaccion");
					this.terminarIteracion();
				}

				this.click(By.xpath("//*[@id=\"cphCuerpo_btnVolver\"]"));
				i++;

			} while ((descripcion.equals("Descuento por Pago a Proveedores")
					|| descripcion.equals("Descuento para pago de Nomina")) && i < resta + 2);

		} else {

			boolean encontrado = false;
			String registroAdenda = "(//*[@id=\"cphCuerpo_gvMovimientosCuentas\"]"
					+ "/tbody/tr[FILA]/td/span[contains(text(), 'DESCRIPCION')])[1]";

			do {
			} while (this.element(btnDatosAdenda) == null);

			this.click(btnDatosAdenda);

			do {
			} while (this.element(tableAdenda) == null);

			DXCUtil.wait(5);

			Evidence.saveAllScreens("1", this);

			registroAdenda = registroAdenda.replace("FILA", "1").replace("DESCRIPCION", descripcion);

			if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
				registroAdenda = registroAdenda.replace("td", "td[4]")
						.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
				DXCUtil.wait(2);
				this.click(this.element(registroAdenda));
				encontrado = true;
			}

			registroAdenda = registroAdenda.replace("tr[1]", "tr[2]").replace("DESCRIPCION", descripcion);

			if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
				registroAdenda = registroAdenda.replace("td", "td[4]")
						.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
				DXCUtil.wait(2);
				this.click(this.element(registroAdenda));
				encontrado = true;
			}

			registroAdenda = registroAdenda.replace("tr[2]", "tr[3]").replace("DESCRIPCION", descripcion);

			if (this.element(By.xpath(registroAdenda)) != null && !encontrado) {
				registroAdenda = registroAdenda.replace("td", "td[4]")
						.replace("/span[contains(text(), '" + descripcion + "')])[1]", "/a").replace("(", "");
				DXCUtil.wait(2);
				this.click(this.element(registroAdenda));
				encontrado = true;
			}

			DXCUtil.wait(4);

			if (encontrado) {

				DXCUtil.wait(4);

				this.NotaDebitoDetalleReferencia(detalleVacio);

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el registro de la transaccion");
				this.terminarIteracion();
			}

			this.click(By.xpath("//*[@id=\"cphCuerpo_btnVolver\"]"));

		}
	}

// ***********************************************************************************************************************
	/**
	 * Realiza el flujo de adenda para la ventana de "Transferencias a traves del
	 * portal"<b><br>
	 * Primer String es la descripcion de la transaccion que se esta ejecutando,
	 * como aparece en la tabla de "Transferencias a traves del portal"<br>
	 * Segundo String es el detalle de como deberia aparecer si se envia el campo
	 * vacio.
	 */
	public void TranferenciaPortalAdenda(String descripcion, String detalleVacio) throws Exception {

		String msgError = this.irAOpcion("Transacciones Realizadas en el Portal PYME", "Consultas",
				"Transacciones a Través del Portal");
		String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		String numAprobacion = SettingsRun.getTestData().getParameter("Número Aprobación").trim();

		boolean encontrado = false;
		xPathLinkNumAprobacion = xPathLinkNumAprobacion.replace("NUM_APROBACION", numAprobacion);

		int j = 0;
		do {
			j++;
			DXCUtil.wait(2);
		} while (this.element(btnConsultasExtractos) == null && j < 6);

		try {
			this.click(btnConsultasExtractos);
		} catch (Exception e) {
		}

		do {
		} while (this.element(btnDatosAdenda) == null);

		this.click(btnDatosAdenda);

		do {
		} while (this.element(tableTxAdenda) == null);

		try {

			DXCUtil.wait(2);

			this.click(this.element(xPathLinkNumAprobacion));
			encontrado = true;

		} catch (Exception e) {
			encontrado = false;
			Reporter.reportEvent(Reporter.MIC_PASS, "Registro no encontrado con numero de aprobacion" + numAprobacion);
		}

		Evidence.saveAllScreens("1", this);

		Reporter.reportEvent(Reporter.MIC_PASS, "------Cuenta Origen------");

		DXCUtil.wait(4);

		if (encontrado && !servicio.equals("Pago a Proveedores") && !servicio.equals("Nómina")) {

			Evidence.saveAllScreens("2", this);

			this.TransferenciaPortalDetalleReferencia(detalleVacio);

		} else if (encontrado && (servicio.equals("Pago a Proveedores") || servicio.equals("Nómina"))) {

			int i = 2;
			String xPathLinkNomProveedores;
			String xPathDataNomProveedores;

			do {

				this.irAOpcion("Transacciones Realizadas en el Portal PYME", "Consultas",
						"Transacciones a Través del Portal");

				do {
				} while (this.element(btnDatosAdenda) == null);

				this.click(this.element(xPathLinkNumAprobacion));

				xPathLinkNomProveedores = xPathLinkNomProv.replace("NUM_REGISTRO", String.valueOf(i));
				xPathDataNomProveedores = xPathDataNomProv.replace("NUM_REGISTRO", String.valueOf(i));

				DXCUtil.wait(4);

				if (this.element(xPathLinkNomProveedores) != null) {

					WebElement locDataNomProveedores = this.findElement(By.xpath(xPathDataNomProveedores));
					String DataNomProveedores = locDataNomProveedores.getText();

					Reporter.reportEvent(Reporter.MIC_PASS, "......" + DataNomProveedores + "......");

					DXCUtil.wait(4);

					this.click(By.xpath(xPathLinkNomProveedores));

					DXCUtil.wait(4);

					this.TransferenciaPortalDetalleReferencia(detalleVacio);

				} else {
					encontrado = false;
				}

				i++;
			} while (encontrado);

		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el registro de la transaccion");
		}

//			this.click(By.xpath("//*[@id=\"cphCuerpo_btnDetalleTransaccionAnterior\"]"));
	}

	public void Cotizacion() throws Exception {
		WebElement elementPaso;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA PARA LA SELECCIÓN DEL ORIGEN
			DXCUtil.wait(1);
			elementPaso = this.element(tituMoCotizar);
		} while (elementPaso == null);

		Evidence.saveFullPage("Cotizacion", this);
		DXCUtil.wait(1);
		this.click(ButtonSigIn);
	}

// ***********************************************************************************************************************
	/**
	 * Realiza la comparacion de los campos detalle y referencia en "Transferencia a
	 * traves del portal"<br>
	 * Valida si los datos son correctos<br>
	 * <b> detalleVacio = como deberia aparecer si se envia el campo "detalle" vacio
	 */
	public void TransferenciaPortalDetalleReferencia(String detalleVacio) throws Exception {

		String dataDescripcion = SettingsRun.getTestData().getParameter("Descripcion").trim();
		String dataReferencia = SettingsRun.getTestData().getParameter("Referencia").trim();

		String detalle;
		String referencia;

		Evidence.saveAllScreens("3", this);

		if (detalleVacio.equals("Pago de Nóminas") || detalleVacio.equals("Pago a Proveedores")) {

			WebElement referenciaElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblEjecutadoReferencia\"]"));
			referencia = referenciaElement.getText();

			WebElement detalleElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblEjecutadoDescripcion\"]"));
			detalle = detalleElement.getText();

		} else {

			WebElement referenciaElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblERReferencia\"]"));
			referencia = referenciaElement.getText();

			WebElement detalleElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblERDescripcion\"]"));
			detalle = detalleElement.getText();

		}

		if (!dataDescripcion.isEmpty()) {

			if (dataDescripcion.equals(detalle)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Descripcion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Descripcion");
			}

		} else {

			if (detalle.equals(detalleVacio)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Descripcion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Descripcion");
			}

		}

		if (!dataReferencia.isEmpty()) {

			if (dataReferencia.equals(referencia)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Referencia");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Referencia");
			}

		} else {

			if (referencia.equals("000000000000000000000000")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Referencia");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Referencia");
			}
		}
	}

// ***********************************************************************************************************************
	/**
	 * Realiza la comparacion de los campos detalle y referencia en "Consultas y
	 * Extractos"<br>
	 * Valida si los datos son correctos<br>
	 * <b> detalleVacio = como deberia aparecer si se envia el campo "detalle" vacio
	 */
	public void NotaDebitoDetalleReferencia(String detalleVacio) throws Exception {

		String dataDescripcion = SettingsRun.getTestData().getParameter("Descripcion").trim();
		String dataReferencia = SettingsRun.getTestData().getParameter("Referencia").trim();

		Evidence.saveAllScreens("2", this);

		DXCUtil.wait(5);

		String detalle = null;
		String referencia = null;

		try {

			WebElement detalleElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblCDDetalle\"]"));
			detalle = detalleElement.getText();

			WebElement referenciaElement = this.findElement(By.xpath("//*[@id=\"cphCuerpo_lblCDReferencia\"]"));
			referencia = referenciaElement.getText();

		} catch (Exception e) {

			do {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el elemento");
				DXCUtil.wait(10);

			} while (true);
		}

		if (!dataDescripcion.isEmpty()) {

			if (dataDescripcion.equals(detalle)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Descripcion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Descripcion");
			}

		} else {

			if (detalle.equals(detalleVacio)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Descripcion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Descripcion");
			}

		}

		if (!dataReferencia.isEmpty()) {

			if (dataReferencia.equals(referencia)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Referencia");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Referencia");
			}

		} else {

			if (referencia.equals("000000000000000000000000")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Adenda correcta en Referencia");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Adenda incorrecta en Referencia");
			}
		}

	}

// ***********************************************************************************************************************
	/**
	 * Revisa los datos cargados de Ordenes de Pago en Consultas/Proceso de Pago.
	 * Valida que los datos coincidan con los datos diligenciados previamente<br>
	 */

	public void RevisionProOrdenesDePago(String numPocesoPago, String nomProcesoPago, String cantidaPagos,
			String valorPagos, String convenioPago) throws Exception {

		Date fecha = new Date();
		Date fechaToday = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, 0);
		Date fechaMañana = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, 1);
		String today = DXCUtil.dateToString(fechaToday, "dd/mm/yyyy");
		String mañana = DXCUtil.dateToString(fechaMañana, "dd/mm/yyyy");
		xpathNumProcesoPago = xpathNumProcesoPago.replace("NUM_PROCESO_PAGO", numPocesoPago);
		int cont = 0;

		this.click(By.xpath("//*[@id=\"formMenu:consultas\"]"));
		this.click(By.xpath("//*[@id=\"formMenu:conProcPago\"]"));

		do {
		} while (!this.isDisplayed(btnBuscarEstadoProceso));
		DXCUtil.wait(3);

		Evidence.save("Ventana de proceso de pago");

		this.click(btnBuscarEstadoProceso);

		// posible do while de displayed

		do {

			cont++;
			xpathNumProcesoPago = xpathNumProcesoPago.replace("NUM", String.valueOf(cont));

			if (this.element(xpathNumProcesoPago) != null) {

				xpathNumProcesoPago = xpathNumProcesoPago.replace("/td[1][contains(text(),'" + numPocesoPago + "')]",
						"");

				break;
			}

		} while (cont <= 3);

		Reporter.write("*** WARNING ------ " + "**************PROCESO DE LA ORDEN DE PAGO****************");

		if (this.element(xpathNumProcesoPago + "/td[3]").getText().equals(mañana)) {
			Reporter.write("*** PASSED :: " + "Fecha Vencimiento correcta");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[3]").getText());
			Reporter.write("*** PASSED ------ " + mañana);
		} else {

			Reporter.write("*** FAIL :: " + "Fecha Vencimiento incorrecta");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[3]").getText());
			Reporter.write("*** FAIL ------ " + mañana);
		}

		if (this.element(xpathNumProcesoPago + "/td[2]").getText().equals(today)) {
			Reporter.write("*** PASSED :: " + "Fecha de Creacion correcta");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[2]").getText());
			Reporter.write("*** PASSED ------ " + today);
		} else {

			Reporter.write("*** FAIL :: " + "Fecha de Creacion incorrecta");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[2]").getText());
			Reporter.write("*** FAIL ------ " + today);
		}

		if (this.element(xpathNumProcesoPago + "/td[10]").getText().equals(today)) {
			Reporter.write("*** PASSED :: " + "Fecha de Aprobacion correcta");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[10]").getText());
			Reporter.write("*** PASSED ------ " + today);
		} else {

			Reporter.write("*** FAIL :: " + "Fecha de Aprobacion incorrecta");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[10]").getText());
			Reporter.write("*** FAIL ------ " + today);
		}

		if (this.element(xpathNumProcesoPago + "/td[1]").getText().equals(numPocesoPago)) {
			Reporter.write("*** PASSED :: " + "Numero del proceso correcto");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[1]").getText());
			Reporter.write("*** PASSED ------ " + numPocesoPago);
		} else {

			Reporter.write("*** FAIL :: " + "Numero del proceso incorrecto");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[1]").getText());
			Reporter.write("*** FAIL ------ " + nomProcesoPago);
		}

		if (this.element(xpathNumProcesoPago + "/td[5]").getText().equals("No Enviado")) {
			Reporter.write("*** PASSED :: " + "Estado correcto");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[5]").getText());
			Reporter.write("*** PASSED ------ " + "No Enviado");
		} else {
			Reporter.write("*** FAIL :: " + "Estado incorrecto");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[5]").getText());
			Reporter.write("*** FAIL ------ " + "No Enviado");
		}

		if (this.element(xpathNumProcesoPago + "/td[4]").getText().equals(nomProcesoPago)) {
			Reporter.write("*** PASSED :: " + "Nombre del proceso correcto");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[4]").getText());
			Reporter.write("*** PASSED ------ " + nomProcesoPago);
		} else {
			Reporter.write("*** FAIL :: " + "Nombre del proceso incorrecto");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[4]").getText());
			Reporter.write("*** FAIL ------ " + nomProcesoPago);
		}

		if (this.element(xpathNumProcesoPago + "/td[6]").getText().equals(cantidaPagos)) {
			Reporter.write("*** PASSED :: " + "Cantidad de pagos correcta");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[6]").getText());
			Reporter.write("*** PASSED ------ " + cantidaPagos);
		} else {
			Reporter.write("*** FAIL :: " + "Cantidad de pagos incorrecta");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[6]").getText());
			Reporter.write("*** FAIL ------ " + cantidaPagos);
		}

		String valorPagosSinCaracteres = this.element(xpathNumProcesoPago + "/td[7]").getText().replace("$", "")
				.replace(",", "").replace(".", "");

		if (valorPagosSinCaracteres.contains(valorPagos)) {
			Reporter.write("*** PASSED :: " + "Valor total de pago correcta");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[7]").getText());
			Reporter.write("*** PASSED ------ " + valorPagos);
		} else {
			Reporter.write("*** FAIL :: " + "Valor total de pago incorrecta");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[7]").getText());
			Reporter.write("*** FAIL ------ " + valorPagos);
		}

		if (this.element(xpathNumProcesoPago + "/td[8]").getText().contains(convenioPago)) {
			Reporter.write("*** PASSED :: " + "Convenio de pago correcto");
			Reporter.write("*** PASSED ------ " + this.element(xpathNumProcesoPago + "/td[8]").getText());
			Reporter.write("*** PASSED ------ " + convenioPago);
		} else {
			Reporter.write("*** FAIL :: " + "Convenio de pago incorrecto");
			Reporter.write("*** FAIL ------ " + this.element(xpathNumProcesoPago + "/td[8]").getText());
			Reporter.write("*** FAIL ------ " + convenioPago);
		}

		Evidence.save("Registro en Proceso de Pago encontrado");

	}

// ***********************************************************************************************************************
	/**
	 * Revisa los datos cargados de Ordenes de Pago en Consultas/Estado de Pago
	 * Valida que los datos coincidan con los datos cargados previamente en el
	 * archivo adjunto<br>
	 */

	public void RevisionEstOrdenesDePago(String numProcesoPago, String cantidadPagos, String convenioPago)
			throws Exception {

		String[] valoresFila = new String[14];

		int cont = 0;

		String xpathNumEstadoPagoMod;
		String xpathNumEstadoPagoMod2;

		xpathNumEstadoPago = xpathNumEstadoPago.replace("NUM_PROCESO_PAGO", numProcesoPago);

		this.click(By.xpath("//*[@id=\"formMenu:consultas\"]"));
		this.click(By.xpath("//*[@id=\"formMenu:conEstadosPago\"]"));

		do {
		} while (!this.isDisplayed(btnBuscarEstadoProceso));
		DXCUtil.wait(3);

		Evidence.save("Ventana en Estado de Pago");

		this.click(btnBuscarEstadoProceso);

		Reporter.write("*** WARNING ------ " + "**************ESTADO DE LA ORDEN DE PAGO****************");

		DXCUtil.wait(3);

		boolean encontrado = false;

		for (int i = 1; i < Integer.parseInt(cantidadPagos) + 1; i++) {

			Reporter.write("*** WARNING ------ " + "--------FILA " + String.valueOf(i) + "----------");

			valoresFila = this.ExtraerDatosDe2doExcel(i + 1);
			xpathNumEstadoPagoMod = xpathNumEstadoPago + "[" + String.valueOf(i) + "]";

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[2]");

			this.click(btnBuscarEstadoProceso);

			int contador = 0;
			int veces = 0;

			do {

				contador++;
				DXCUtil.wait(1);

				if (contador == 10) {
					veces++;
					contador = 0;
					this.click(btnBuscarEstadoProceso);
				}

			} while (!this.isDisplayed(By.xpath(xpathNumEstadoPagoMod2)) && contador < 11 && veces < 4);

			if (!this.isDisplayed(By.xpath(xpathNumEstadoPagoMod2))) {
				Reporter.write("No se encuentra El registro con numeor de proceso: " + numProcesoPago);
				SettingsRun.exitTestIteration();
			}

			if (this.element(xpathNumEstadoPagoMod2).getText().equals(numProcesoPago)) {
				Reporter.write("*** PASSED :: " + "Numero de proceso correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + numProcesoPago);
			} else {
				Reporter.write("*** FAIL :: " + "Numero de proceso incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + numProcesoPago);
			}

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[3]").replace(numProcesoPago, "");

			if (this.element(xpathNumEstadoPagoMod2).getText().equals(valoresFila[1])) {
				Reporter.write("*** PASSED :: " + "Id Beneficiario correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + valoresFila[1]);
			} else {
				Reporter.write("*** FAIL :: " + "Id Beneficiario incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + valoresFila[1]);
			}

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[4]").replace(numProcesoPago, "");

			if (this.element(xpathNumEstadoPagoMod2).getText().equals(valoresFila[2])) {
				Reporter.write("*** PASSED :: " + "Beneficiario correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + valoresFila[2]);
			} else {
				Reporter.write("*** FAIL :: " + "Beneficiario incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + valoresFila[2]);
			}

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[6]").replace(numProcesoPago, "");

			if (this.element(xpathNumEstadoPagoMod2).getText().equals("PAGO NACIONAL")) {
				Reporter.write("*** PASSED :: " + "Destino correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + "PAGO NACIONAL");
			} else {
				Reporter.write("*** FAIL :: " + "Destino incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + "PAGO NACIONAL");
			}

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[7]").replace(numProcesoPago, "");

			if (this.element(xpathNumEstadoPagoMod2).getText().contains(convenioPago)) {
				Reporter.write("*** PASSED :: " + "Convenio correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + convenioPago);
			} else {
				Reporter.write("*** FAIL :: " + "Convenio incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + convenioPago);
			}

			xpathNumEstadoPagoMod2 = xpathNumEstadoPagoMod.replace("td[2]", "td[8]").replace(numProcesoPago, "");
			String valorPagosSinCaracteres = this.element(xpathNumEstadoPagoMod2).getText().replace("$", "")
					.replace(",", "").replace(".", "");

			if (valorPagosSinCaracteres.contains(valoresFila[8])) {
				Reporter.write("*** PASSED :: " + "Valor correcto");
				Reporter.write("*** PASSED ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** PASSED ------ " + valoresFila[8]);
			} else {
				Reporter.write("*** FAIL :: " + "Valor incorrecto");
				Reporter.write("*** FAIL ------ " + this.element(xpathNumEstadoPagoMod2).getText());
				Reporter.write("*** FAIL ------ " + valoresFila[8]);
			}

		}

		Evidence.save("Registro en Estado de Pago encontrado");

	}

// ***********************************************************************************************************************
	/**
	 * Extrae los datos del archivo adjunto de Ordenes de Pago. Int = numero de la
	 * fila de datos que se va a extraer<br>
	 * Retorna los datos en una array
	 */

	public String[] ExtraerDatosDe2doExcel(int fila) throws Exception {

		String rutaArchivoOrpa = SettingsRun.getTestData().getParameter("Archivo Destinos");
		String[] valoresFila = new String[14];

		try {

			// Crear un objeto FileInputStream para leer el archivo Excel
			FileInputStream fis = new FileInputStream(new File(rutaArchivoOrpa));

			// Crear un objeto XSSFWorkbook para representar el libro Excel
			XSSFWorkbook workbook = new XSSFWorkbook(fis);

			// Obtener la hoja de trabajo (sheet) por índice o nombre
			XSSFSheet sheet = workbook.getSheetAt(0); // Puedes cambiar el índice si tienes varias hojas

			// Obtener la fila deseada
			Row row = sheet.getRow(fila - 1); // Restar 1 porque los índices de fila comienzan desde 0

			String valfila;

			for (int i = 0; i < 13; i++) {
				DXCUtil.wait(1 / 4);
				Cell cell = row.getCell(i);
				valfila = cell.toString();
				valoresFila[i] = valfila;
			}

			// Cerrar el flujo de entrada
			fis.close();
			// Cerrar el libro de Excel
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return valoresFila;

	}

	/**
	 * Realiza la validacion de los saldos Iniciales en Stratus comparando los saldo
	 * Totales del Portal
	 * 
	 * @param tipoDocumento
	 * @param numeroDoc
	 * @param tipoCta
	 * @param numCta
	 * @param saldoOrigen
	 * @throws Exception
	 */
	public void validacionSaldosStratus(String servicio,String tipoDocumento,String numeroDoc,String tipoCta, String numCta, boolean saldoOrigen) throws Exception {
		if (DatosDavivienda.STRATUS != null) {
//			String tipoDocumento = SettingsRun.getTestData().getParameter("Tipo ID Empresa");
//			String numeroDoc = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
			String saldoDis = null;
			String saldoDisponible = null;
			List<String> saldoscuentasCredi = new ArrayList<>();

			if (saldoOrigen) {
//				String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();

				if (!servicio.contains("Servicios")) {

					if (!servicio.contains("Mismo NIT") && !servicio.contains("TC"))
						saldoDis = this.getText(this.element(saldoDisXpath
								.replace("NUMCUENTA", DXCUtil.right(numCta, 4)).replace("TIPOCUENTA", tipoCta)));
					else if ((servicio.contains("Mismo NIT") || servicio.contains("TC"))
							&& !tipoCta.contains("Familiares"))
						saldoDis = this.getText(this.element(saldoDisXpathMismoNit
								.replace("NUMCUENTA", DXCUtil.right(numCta, 4)).replace("TIPOCUENTA", tipoCta)));

					if (tipoCta.contains("Familiares")) {
						saldoDis = this.getText(
								this.element(saldoDisServiciosXpath2.replace("NUMCUENTA", DXCUtil.right(numCta, 4))
										.replace("TIPOCUENTA", tipoCta).replace("NUM", "5")));

					}

					saldoDisponible = saldoDis.replace("$ ", "").replace(".", "").replace(",", ".");

				} else if (servicio.contains("Servicios")) {
					saldoDis = this
							.getText(this.element(saldoDisServiciosXpath2.replace("NUMCUENTA", DXCUtil.right(numCta, 4))
									.replace("TIPOCUENTA", tipoCta).replace("NUM", "4")));

					saldoDisponible = saldoDis.replace("$ ", "").replace(".", "").replace(",", ".");
				}

				if (!tipoCta.contains("CREDITO")) {

					List<WebElement> listaCrediexpress = this.findElements(By.xpath(saldoDisXpath
							.replace("NUMCUENTA", DXCUtil.right(numCta, 4)).replace("TIPOCUENTA", "Crediexpress")));

					for (WebElement saldoCredi : listaCrediexpress) {

						saldoscuentasCredi
								.add(saldoCredi.getText().replace("$ ", "").replace(".", "").replace(",", "."));

					}
				}

			}

			if (!DatosDavivienda.IS_RISKMOTOR) {
				
				String datoValidar = DXCUtil.left(numCta, 3);
				
				if ((tipoCta.contains("DIPLUS") || tipoCta.contains("diplus"))&& datoValidar.equals("056")) {
					tipoCta = "CREDITO";

					String[] datosCrediplus = DatosDavivienda.STRATUS.getCtaCobroCrediplus(numCta);
					tipoCta = datosCrediplus[0];

					if (tipoCta.contains("AH")) {
						tipoCta = "CC";
					}
				}

				else if ((tipoCta.contains("DIPLUS") || tipoCta.contains("diplus")) && !datoValidar.equals("056")) {
					tipoCta = "CREDITO";
					String[] datosCrediplus = DatosDavivienda.STRATUS.getCtaCobroCrediplus(numCta);
					tipoCta = datosCrediplus[0];
					numCta = datosCrediplus[1];

				}

				if (tipoCta.contains("ompanita") || tipoCta.contains("emp pyme 22")) {
					tipoCta = "CREDITO";
				}

				String[] saldosDisponibles = DatosDavivienda.STRATUS.consultarDatosPantallaSaldos(tipoDocumento,
						numeroDoc, tipoCta, numCta);

				if (saldoOrigen) {

					// Elimina el carácter '$' y espacios de ambas cadenas antes de formatear
					if (tipoCta.contains("ahorro") || tipoCta.contains("AH")) {
						this.saldoTotalInicial = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[1].replace("$ ", "");
					}
					if (tipoCta.contains("corriente") || tipoCta.contains("CC")) {
						this.saldoTotalInicial = saldosDisponibles[6].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[0].replace("$ ", "");

					}

					if (tipoCta.contains("CREDITO")) {
						this.saldoTotalInicial = saldosDisponibles[7].replace("$ ", "");
						this.saldoDispoInicial = saldosDisponibles[2].replace("$ ", "");
					}
					if (tipoCta.contains("Familiares")) {
						this.saldoDispoInicial = saldosDisponibles[0].replace("$ ", "");
						this.saldoTotalInicial = saldosDisponibles[1].replace("$ ", "");
					}

					if (!tipoCta.contains("CREDITO")) {

						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo stratus Total Inicial: " + this.saldoTotalInicial.replace(".", ","));
						Reporter.reportEvent(Reporter.MIC_INFO, "El saldo stratus Disponible Efectivo Inicial: "
								+ this.saldoDispoInicial.replace(".", ","));
					} else {
						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo stratus Cupo disponible Crediexpress rotativo Inicial: "
										+ this.saldoTotalInicial.replace(".", ","));
						Reporter.reportEvent(Reporter.MIC_INFO, "El saldo stratus Crediexpress rotativo Inicial: "
								+ this.saldoDispoInicial.replace(".", ","));
					}

				} else if (!saldoOrigen) {

					// Elimina el carácter '$' y espacios de ambas cadenas antes de formatear
					if (tipoCta.contains("ahorro") || tipoCta.contains("AH")) {
						this.saldoTotalFinal = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[1].replace("$ ", "");
					}

					if (tipoCta.contains("corriente") || tipoCta.contains("CC")) {
						this.saldoTotalFinal = saldosDisponibles[6].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[0].replace("$ ", "");
					}

					if (tipoCta.contains("CREDITO")) {

						this.saldoTotalFinal = saldosDisponibles[7].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[2].replace("$ ", "");
					}

					if (tipoCta.contains("Familiares")) {
						this.saldoTotalFinal = saldosDisponibles[0].replace("$ ", "");
						this.saldoDispoFinal = saldosDisponibles[1].replace("$ ", "");
					}

					if (!tipoCta.contains("CREDITO")) {

						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo Total stratus Final: " + this.saldoTotalFinal.replace(".", ","));
						Reporter.reportEvent(Reporter.MIC_INFO, "El saldo Disponible Efectivo stratus Final: "
								+ this.saldoDispoFinal.replace(".", ","));
					} else {
						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo stratus Cupo disponible Crediexpress rotativo Final: "
										+ this.saldoTotalFinal.replace(".", ","));
						Reporter.reportEvent(Reporter.MIC_INFO, "El saldo stratus Crediexpress rotativo Final: "
								+ this.saldoDispoFinal.replace(".", ","));

						this.saldoTotalFinal = this.saldoDispoFinal;
					}
				}

				double cupoCredi = 0.0;
				double sumasaldoDispCred = 0.0;
				double restasaldoDispCred = 0.0;
				if (saldoOrigen) {

					if (!tipoCta.contains("CREDITO")) {

						if (this.saldoTotalInicial.contains(saldoDisponible)) {
							Reporter.reportEvent(Reporter.MIC_INFO,
									"El saldo total en stratus " + saldoTotalInicial.replace(".", ",")
											+ " corresponde al saldo disponible en el portal: " + saldoDis);
						} else {
							Reporter.reportEvent(Reporter.MIC_INFO, "El saldo total en stratus " + saldoTotalInicial
									+ " no corresponde al saldo disponible en el portal:" + saldoDis);
						}

					} else {
						Reporter.reportEvent(Reporter.MIC_INFO, "==========[SALDOS DISPONIBLES DE LA CUENTA  " + numCta
								+ " - CONSULTAS]=======================================================================================================");
						for (String suma : saldoscuentasCredi) {
							cupoCredi = sumasaldoDispCred += Double.parseDouble(suma);
							Reporter.reportEvent(Reporter.MIC_INFO, "saldo Disponible: " + suma.replace(".", ","));
							if (!saldoDisponible.equals(suma)) {
								restasaldoDispCred = (Double.parseDouble(suma) - cupoCredi);
							}
						}

						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo corresponde al cupo disponible Stratus: "
										+ saldoTotalInicial.replace(".", ",")
										+ " corresponde a la sumatoria de los saldos al cupo disponible del portal: "
										+ BigDecimal.valueOf(sumasaldoDispCred));

						double saldodispoCredSele = 0.0;

						saldodispoCredSele = Double.parseDouble(saldoDisponible);
						restasaldoDispCred = Double
								.parseDouble(String.valueOf(BigDecimal.valueOf(restasaldoDispCred)).replace("-", ""));
						if (restasaldoDispCred == saldodispoCredSele) {
							Reporter.reportEvent(Reporter.MIC_INFO,
									"El saldo disponible " + BigDecimal.valueOf(restasaldoDispCred)
											+ " corresponde al saldo disponible en el portal: " + saldoDis);
						} else {
							Reporter.reportEvent(Reporter.MIC_INFO,
									"El saldo total en stratus " + BigDecimal.valueOf(restasaldoDispCred)
											+ " no corresponde al saldo disponible en el portal:" + saldoDis);
						}

						Reporter.reportEvent(Reporter.MIC_INFO,
								"El saldo Inicial: " + this.saldoDispoInicial.replace(".", ","));
						this.saldoTotalInicial = this.saldoDispoInicial;

					}

				}
			}

			DatosDavivienda.STRATUS.minimizarVentana();
		}
	}

	public String getSaldoTotalInicialOrigen() {
		return this.saldoTotalInicial;
	}

	public String getSaldoDispoInicialOrigen() {
		return this.saldoDispoInicial;
	}

	public String getSaldoTotalFinalOrigen() {
		return this.saldoTotalFinal;
	}

	public String getSaldoDispoFinalOrigen() {
		return this.saldoDispoFinal;
	}
}