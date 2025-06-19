package dav.pymes.moduloCrearTx;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import dav.pymes.PagePortalPymes;
import dav.pymes.PageLoginPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageValores extends PagePortalPymes {
	
	PageDestino pageDestino;
	ControllerCrearTx ControllerCrearTx;
	ControllerDestinosMasivos ControllerDestinosMasivos;

	By locPasoPage = By.xpath("//td[@class='TituloRojo']/b[text()='Valores']");
	By locButtonSig = By.xpath("//input[contains(@value, 'Siguiente')]");
	// LOCATOR DESCRIPCIÓN PARA ANÓMINA Y PROVEEDORES
	By locCmDescrNP = By.id("cphCuerpo_txtVCdescripcion");
	// LOCATORS PARA DATOS DE OTRAS TRANSACCIONES
	By locCmValor = By.xpath("//input[@id='cphCuerpo_txtValor']");
	By locCmDescr = By.xpath("//input[@id='cphCuerpo_txtDescripcion']");
	By locCmRefer = By.xpath("//input[@id='cphCuerpo_txtReferencia']");
	By locCmReferinsc = By.xpath("//input[@id='cphCuerpo_txtReferencia']");
	By locCmReferCuentaNo = By.xpath("//input[@id='cphCuerpo_txtreferencia']");
	By locCmcphCuerpo_ddl_Meses = By.xpath("//select[@id='cphCuerpo_ddl_Meses' or @id='cphCuerpo_ddl_Meses']");
	By locCmctxtAño = By.id("cphCuerpo_txtAño");
	By locCmctxtPlazo = By.id("cphCuerpo_txtPlazo");
	// LOCATORS VALOR PAGO PARA SERVICIOS PÚBLICOS
	By locCmValorFact1 = By.xpath("//input[@id='cphCuerpo_TxtValorAPagar']");
	By locCmValorFact2 = By.xpath("//span[@id='cphCuerpo_lbValorPagar']");
	// LOCATORS VALOR PAGO PARA SERVICIOS PÚBLICOS AUTOMATICOS
	By locCmcorreonotificaciones = By.id("cphCuerpo_tbxcorreonotificaciones");
	By locCmValorapagar = By.id("cphCuerpo_tbxvalorpagar");
	By locCmmaximodescontar = By.id("cphCuerpo_tbxtopemaximodescontar");
	By locCmDiadelPago = By.id("cphCuerpo_dropdiadelpago");
	By locCmAbonoExtraordinario = By.id("cphCuerpo_ddlAbonoExtraordinario");
	// LOCATORS PARA ORDENES DE PAGO
	By locConvenioPago = By.id("formPincipal:inIdConvenio");
	By locNombreProcesoPago = By.id("formPincipal:inNombreProceso");
	By locCantidadPagos = By.id("formPincipal:inCantidadPagos");
	By locValorTotalPagos = By.id("formPincipal:inValoPagar");
	By locNumeroProcesoOrpa = By.xpath("//*[@id=\"formPincipal:outNumeroProceso\"]");
	By locArchivoOrpa = By.xpath("//*[@id=\"formPincipal:inRutaArchivo\"]");
	By btnContinuarOrdenesDePago = By.xpath("//*[@id=\"formPopUp\"]/table[2]/tbody/tr/td[1]/input");
	By locCmTokenOtpOrdenesDePago = By.xpath("//*[@id=\"formPincipal:claveToken\"]");
	
	By locTipoArchivo = By.xpath("//*[@id=\"formPincipal:inIdPlantilla\"]");
	
	By btnBuscarArchivo = By.id("//*[@id=\"formPincipal:upload1\"]");
	By btnCargarArchivo = By.id("//*[@id=\"formPincipal:importar\"]");
	// Avance Tc
	By btnAccetarPopup = By.id("cphCuerpo_BtnAceptar");

//=======================================================================================================================
	/**
	 * Constructor a partir del [BasePageWeb] padre.
	 */
	public PageValores(BasePageWeb parentPage) {
		super(parentPage);
	}

//***********************************************************************************************************************
	/**
	 * Pantalla donde sólo solicita la descripción, por lo general pantalla para
	 * Nómina y Proveedores.<br>
	 * Este método ingresa el dato y da click en el botón "Siguiente", no valida si
	 * sale o no error después.
	 */
	public void ingresarDescripcion(String descripcion) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		this.write(locCmDescrNP, descripcion);
		Evidence.saveFullPage("Descripcion", this);
		this.click(locButtonSig);
	}

//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una
	 * referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida
	 * si sale o no error después.
	 */
	public void ingresarValores(String valor, String descripcion, String referencia) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
			DXCUtil.wait(1);
			this.write(locCmValor, valor);
		} while (!this.isDisplayed(locCmValor));

		this.write(locCmDescr, descripcion);
		this.write(locCmRefer, referencia);
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
		DXCUtil.wait(3);
		this.existDialog();
	}

	// ***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una
	 * referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida
	 * si sale o no error después.
	 */
	public void ingresarValoresPropios(String valor) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
			DXCUtil.wait(1);
			this.write(locCmValor, valor);
		} while (!this.isDisplayed(locCmValor));
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
		DXCUtil.wait(5);
		if (this.element(locCmAbonoExtraordinario) != null) {
			Evidence.saveFullPage("Valores bono Extraor dinario", this);
			this.click(locButtonSig);
		}
		DXCUtil.wait(3);
		this.existDialog();
	}

// ***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una
	 * referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida
	 * si sale o no error después.
	 */
	public void ingresarValoresCuentaNo(String valor, String descripcion, String referencia) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		do {
			DXCUtil.wait(1);
			this.write(locCmValor, valor);
		} while (!this.isDisplayed(locCmValor));
		this.write(locCmDescr, descripcion);
		this.write(locCmReferCuentaNo, referencia);
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
		DXCUtil.wait(3);
		this.existDialog();
	}

// ***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una
	 * referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida
	 * si sale o no error después.
	 */
	public void ingresarValoresCuentaIsncr(String valor, String descripcion, String referencia) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		do {
			DXCUtil.wait(1);
			this.write(locCmValor, valor);
		} while (!this.isDisplayed(locCmValor));
		this.write(locCmDescr, descripcion);
		this.write(locCmReferinsc, referencia);
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
		DXCUtil.wait(3);
		this.existDialog();
	}

//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita valor de la transacción, una descripción y una
	 * referencia.<br>
	 * Este método ingresa los datos y da click en el botón "Siguiente", no valida
	 * si sale o no error después.
	 */
	public void ingresarValoresAvanceTC(String valor, String descripcion, String mesVenTC, String aVenTC,
			String plazodifAv) throws Exception {

		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
		do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
			DXCUtil.wait(1);
			this.write(locCmValor, valor);
		} while (!this.isDisplayed(locCmValor));

		this.write(locCmDescr, descripcion);
		this.selectListItem(locCmcphCuerpo_ddl_Meses, mesVenTC);
		this.write(locCmctxtAño, aVenTC);
		this.write(locCmctxtPlazo, plazodifAv);
		Evidence.saveFullPage("Valores", this);
		this.click(locButtonSig);
		DXCUtil.wait(3);
		this.existDialog();
		DXCUtil.wait(1);
		String msgResp = this.getMsgAlertIfExist("cphCuerpo_pnlMensaje");// cambiar el extraer mensaje popup

		if (msgResp != null) {
			Reporter.write(msgResp);
			Evidence.saveFullPage(msgResp, this);
			DXCUtil.wait(1);
			this.click(btnAccetarPopup);
		}
	}

//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita o NO el valor, por lo general pantalla para
	 * Servicios.<br>
	 * Este método ingresa el dato (si se pide) y da click en el botón "Siguiente",
	 * no valida si sale o no error después.<br>
	 * Retorna el valor de la transacción, en caso que el dato se presente y no pida
	 * ingreso.
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
		Evidence.save("ValorTx");
//		Evidence.save("ValorTx", this);
		this.click(locButtonSig);
		return valor;
	}
	
//***********************************************************************************************************************
	/**
	 * Pantalla donde solicita los datos de la transaccion en Ordenes de Pago.
	 * Este método ingresa los datos, da click en el botón "Siguiente" y carga el archivo de ORPA.
	 * Llega hasta la aprobacion OTP/Token. 
	 */
	
	public String ingresarValoresOrpa(String convenioPago, String nomProcesoPago, String cantidaPagos, String valorPagos, String rutaArchivoOrpa) throws Exception {

		String archivoCargar = null;
		String archivoDest = SettingsRun.getTestData().getParameter("Archivo Destinos").trim();
		String tipoCarga = SettingsRun.getTestData().getParameter("Tipo de Carga").trim();
		String filasATomar = SettingsRun.getTestData().getParameter("Filas a Tomar").trim();
		
		List<Integer> listRowExcel;
		String msgError;
		
		this.getDriver().switchTo().frame("cphCuerpo_frameORPA");
		
		do {
		} while (!this.isDisplayed(locConvenioPago ));
		
		this.selectListItem(locConvenioPago, convenioPago);

		this.write(locNombreProcesoPago , nomProcesoPago);
		this.write(locCantidadPagos , cantidaPagos);
		this.write(locValorTotalPagos  , valorPagos);
		
		Evidence.save("Valores ingresados");
		
		do {
		} while (!this.isDisplayed(By.id("formPincipal:continuar")));
		DXCUtil.wait(5);
		
		String scriptReloadPage = "document.getElementById('formPincipal:continuar').click()";
		this.getJse().executeScript(scriptReloadPage);
		
		do {
		} while (!this.isDisplayed(By.id("formPincipal:inRutaArchivo")));
		DXCUtil.wait(5);
		
        WebElement elemNumProceso = this.findElement(locNumeroProcesoOrpa);
        String numProcesoOrpa = elemNumProceso.getAttribute("value");
		
		this.selectListItem(locTipoArchivo, "Excel");
		this.element(By.id("formPincipal:inRutaArchivo")).click();
		
		String scriptReloadPage2 = "document.getElementById('formPincipal:upload1').click()";
		this.getJse().executeScript(scriptReloadPage2);
		
		DXCUtil.wait(5);

		this.adjuntarArchivoOrpa();
		
		do {
		} while (!this.isDisplayed(By.id("formPincipal:importar")));
		DXCUtil.wait(5);
		
		Evidence.save("Ruta cargada");
		
		String scriptReloadPage3 = "document.getElementById('formPincipal:importar').click()";
		this.getJse().executeScript(scriptReloadPage3);
		
		do {
		} while (!this.isDisplayed(btnContinuarOrdenesDePago));
		DXCUtil.wait(5);
		
		Evidence.save("1er PopUp");
		
		this.element(btnContinuarOrdenesDePago).click();
		
		do {
		} while (!this.isDisplayed(btnContinuarOrdenesDePago));
		DXCUtil.wait(5);
		
		Evidence.save("2do PopUp");
		
		this.element(btnContinuarOrdenesDePago).click();
		
		do {
		} while (!this.isDisplayed(locCmTokenOtpOrdenesDePago));
		
		return numProcesoOrpa;
		
	}
	
// ***********************************************************************************************************************
	/**
	 * Pantalla donde solicita o NO el valor, por lo general pantalla para
	 * Servicios.<br>
	 * Este método ingresa el dato (si se pide) y da click en el botón "Siguiente",
	 * no valida si sale o no error después.<br>
	 * Retorna el valor de la transacción, en caso que el dato se presente y no pida
	 * ingreso.
	 * 
	 * @param diaDiaDelpago
	 * @param valor2
	 */
	public String ingresarValorAuto(String correo, String valor, String diaDiaDelpago) throws Exception {
		this.waitPantallaValores(); // ESPERA A QUE ESTÉ EN LA PANTALLA REQUERIDA
//		Evidence.save("Pantalla Valores", this);
		Evidence.save("Pantalla Valores");
		int contador = 0;
		String msg = null;
		WebElement objCm = null;
		do {
			DXCUtil.wait(1);
			objCm = this.element(locCmcorreonotificaciones); // CAMPO DE ENTRADA = INPUT
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOu - No se encontro el campo: Correo para notificaciones");
				SettingsRun.exitTestIteration();
			}
		} while (objCm == null);

		if (objCm != null) // SE DEBE INGRESAR EL VALOR
			this.write(objCm, correo);
		
		DXCUtil.wait(5);
		if (this.element(locCmValorapagar) != null) {
			
		do {
			DXCUtil.wait(1);
			objCm = this.element(locCmValorapagar); // CAMPO DE ENTRADA = INPUT
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut No se encontro el campo: Valor a pagar");
				SettingsRun.exitTestIteration();
			}
		} while (objCm == null);
		
		if (objCm != null) // SE DEBE INGRESAR EL VALOR
			this.write(objCm, valor);
		}

		
		DXCUtil.wait(6);
		if (this.element(locCmmaximodescontar) != null) { // SE DEBE INGRESAR EL VALOR LIMITE
			do {
				DXCUtil.wait(1);
				objCm = this.element(locCmmaximodescontar); // CAMPO DE ENTRADA = INPUT
				contador++;
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut No se encontro el campo: Valor  maximo a descontar");
					SettingsRun.exitTestIteration();
				}
			} while (objCm == null);
			this.write(objCm, valor);
		}
		
		if (this.element(locCmDiadelPago) != null) {
			
		do {
			DXCUtil.wait(1);
			objCm = this.element(locCmDiadelPago); // CAMPO DE ENTRADA = INPUT
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut No se encontro el campo: Día del pago");
				SettingsRun.exitTestIteration();
			}
		} while (objCm == null);
		if (objCm != null) { // SE DEBE INGRESAR EL VALOR
//			String msg = this.selectListContainsItems(objCmValor, diaDiaDelpago);
			msg = this.selectListItemExacto(objCm, diaDiaDelpago);
		}


			if (!msg.equals("")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro el valor: " + msg);
				SettingsRun.exitTestIteration();
			}

		}
//		Evidence.save("ValorTx", this);
		Evidence.save("ValorTx");
		DXCUtil.wait(1);
		do {
			DXCUtil.wait(1);
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut No se encontro el boton siguiente");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(locButtonSig) == null);
		this.click(locButtonSig);
		return valor;
	}

//***********************************************************************************************************************
	/**
	 * Método para esperar a que la pantalla Web se encuentre en "Valores".
	 * 
	 * @throws Exception
	 */
	private void waitPantallaValores() throws Exception {
		WebElement elementPaso;
		int a = 1;
		do { // GARANTIZA QUE SE ENCUENTRA EN LA PANTALLA "Valores"
			DXCUtil.wait(1);
			elementPaso = this.element(locPasoPage);
			a++;
			if (a > 60) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				SettingsRun.exitTestIteration();
				this.closeAllBrowsers();
			}
		} while (elementPaso == null);
//		Evidence.save("PantallaValores", this);
		Evidence.save("PantallaValores");
	}
//***********************************************************************************************************************
	/**
	 * Método para adjuntar el archivo de ORPA. 
	 * 
	 * @throws Exception
	 */
	public void adjuntarArchivoOrpa() throws Exception {
		
		String formatoArchivo = SettingsRun.getTestData().getParameter("Tipo de Carga");
        String rutaArchivo = SettingsRun.getTestData().getParameter("Archivo Destinos");
        String tipoPrueba = SettingsRun.getTestData().getParameter("Servicio");

        DXCUtil.wait(2);
        
        if ((tipoPrueba.equals("ORPA")||tipoPrueba.equals("Administración del efectivo")) && !rutaArchivo.isEmpty()) {

            DXCUtil.wait(3);

            try {
                Robot action = new Robot();

                StringSelection selection = new StringSelection(rutaArchivo);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

                action.keyPress(KeyEvent.VK_CONTROL);
                action.keyPress(KeyEvent.VK_V);

                action.keyRelease(KeyEvent.VK_CONTROL);
                action.keyRelease(KeyEvent.VK_V);

                action.keyPress(KeyEvent.VK_ENTER);
                action.keyRelease(KeyEvent.VK_ENTER);
                
                DXCUtil.wait(2);
                
                Evidence.save("Archivo adjunto");

            } catch (Exception e) {
                Reporter.reportEvent(Reporter.MIC_FAIL, "Error, verifique o comuniquese con automatizador de dxc.");
                e.printStackTrace();
            }

        } else {
            Reporter.reportEvent(Reporter.MIC_FAIL,
                    "No se ha encontrado una ruta esfecifica para realizar el cargue, por favor verifique el documento.");
        }
	}
	
}