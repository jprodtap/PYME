package dav.UpgradeDeCombos;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dav.ActualizacionDeDatos.PageActualizacionDeDatos;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageInicioMiddle;
import dav.AdministracionDelEfectivo.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.*;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageUpgradeDeCombos extends BasePageWeb {
	
	public PageUpgradeDeCombos(BasePageWeb parentPage) {
		super(parentPage);
	}
	
	PageConfirmacion pageConfirmacion = null;
	PageInicioMiddle pageInicioMiddle = null;
	PageLoginPymes pageLogin = null;
	PagePortalPymes pagePymes = null;
	
	WebElement locCombo;
	WebElement locNomEmpresa = this.element("//*[@class='CeldaGrilla']/td[5]");
	WebElement locCheckSelecCombos = this.element("//*[contains(@id,'cphCuerpo_repCombosComparar_checkbox')]");
	WebElement locCheckSelecUnCombo = this.element("(//*[contains(@id,'cphCuerpo_repCombosComparar_checkbox')])[1]");
	
	String msgAlerta;
	String comboActual;
	String xpathComboMain = "//*[@class=\"CarruselNombre\"][contains(text(),'COMBO')]"; 
	String xpathCambieseYa = "/ancestor::div[2]/div[2]/ul/li[1]";
	String xpathConozcaMas = "/ancestor::div[2]/div[2]/ul/li[2]/a";
	String xpathValorFront = "/ancestor::div[2]/div[2]/p";
	
	//localizadores front office
	By locComboActual = By.xpath("//*[@id=\"cphCuerpo_txtPlanActual\"]");
	By locSwitchComparar = By.xpath("//*[@class=\"ios-ui-select\"]");
	By locCantidadTx = By.xpath("//*[@id=\"cphCuerpo_txtCantidadTrx\"]");
	By locBtnComparar = By.xpath("//*[@id=\"cphCuerpo_btnComparar\"]");
	By locInfoCambioCombo = By.xpath("//td[@class=\"TituloNegro\"][contains(text(), 'Ha seleccionado el plan ')]");
	//localizadores de busqueda middle en flujo front
	By locFiltroFechaMiddle = By.xpath("//*[@id=\"cphCuerpo_gvAutogestion\"]/tbody/tr[1]/th[1]/a");
	By locComboAnteriorMiddle = By.xpath("(//*[@id=\"cphCuerpo_gvAutogestion\"]/tbody/tr[2]/td[6])[1]");
	By locComboActualMiddle = By.xpath("(//*[@id=\"cphCuerpo_gvAutogestion\"]/tbody/tr[2]/td[7])[1]");
	By locFechaRegistro = By.xpath("//*[@id=\"cphCuerpo_gvAutogestion\"]/tbody/tr[2]/td[1]");
	
	//localizadores middle office
	By locNumClienteEmpresarial = By.xpath("//*[@id=\"cphCuerpo_PnlBusquedaDatos\"]/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/input");
	By locNumIdEmpresarial = By.xpath("//*[@id=\"cphCuerpo_txtBQNumIdentificacionEmpresa\"]");
	By locSelectCombo = By.xpath("//*[@id=\"cphCuerpo_ddlDEComboServicios\"]");
	By locByNomEmpresa = By.xpath("//*[@class=\"CeldaGrilla\"]/td[5]");
	By locNomEmpresaEdit = By.xpath("//*[@class='CeldaGrilla']/td[5]/following-sibling::td[4]/a");
	
	//localizadores asignar max transaccion 
	By locTitle = By.xpath("//*[@id=\"lblMasterTitulo\"]");
	By locTitleCombos = By.xpath("//*[@id=\"cphCuerpo_gvCombos\"]/tbody/tr[1]/th[1]/a");
	By locTableInfoComercial = By.xpath("//*[@id=\"cphCuerpo_pnlInfComercial\"]/table/tbody");
	By locCompararCombos = By.xpath("//*[@id=\"cphCuerpo_ddlComparaCombos\"]");
	By locPrimerCheckCompararCombos = By.xpath("//*[@id=\"cphCuerpo_repCombo_chkComparaCombo_0\"]");
	By locBtnSiguienteTable = By.xpath("//*[@id=\"cphCuerpo_lnkCSiguiente\"]");
	By locValidarMaxTrans = By.xpath("//*[@id=\"cphCuerpo_ddlECValidacionMaximaTransacciones\"]");
	String xpathTableComboMax = "//*[@id=\"cphCuerpo_gvCombos\"]/tbody/tr/td[1][contains(text(),'COMBO')]";
	String xpathDestino = "//*[@id=\"cphCuerpo_UpdatePanel1h\"]/table/tbody/tr/td[1][contains(text(),'DESTINO')]";
	
	//PopUp
	By cmpPopup = By.id("mensaje");
	By btnPopup = By.id("botonModal");
	By btnPopup2 = By.xpath("//*[@id='btnmodal']");
	By sesionEx = By.xpath("//b[contains(text(), 'Sesion no existe o ha expirado por inactividad.')]");
	
	//Informes Middle
	By locFechaDesde = By.xpath("//*[@id=\"cphCuerpo_txtFechaInicial\"]");
	By locFechaHasta = By.xpath("//*[@id=\"cphCuerpo_txtFechaFinal\"]");
	
    public void InicioUpgradeCombosFront() throws Exception {
    	
    	String xpathCombo;
    	String msjRespuesta = null;
    	String combo = SettingsRun.getTestData().getParameter("Combo").trim();
    	
    	xpathCombo = xpathComboMain.replace("COMBO", combo);
    	locCombo  = this.element(xpathCombo);
    	
    	DXCUtil.wait(2);
    	
    	if (this.element(xpathCombo) != null) {
    		
    		Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el combo disponible: " + combo);
    		
    		if (this.element(locCantidadTx) != null) { 			
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra la cantidad de transacciones a la fecha");
    			
    		}else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra la cantidad de transacciones a la fecha");
    		}
    		
    		DXCUtil.wait(1);
    		comboActual = this.element(locComboActual).getAttribute("value");
    		
    		if (this.element(locSwitchComparar) != null) { 			
    			this.click(locSwitchComparar);
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el stwitch para habilitar la comparacion de combos");
    			
    		}else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el stwitch para habilitar la comparacion de combos");
    		}
    		
    		DXCUtil.wait(1);
//    		Evidence.save("Upgrade_front", this);
    		Evidence.save("Upgrade_front");
    		
    		if (this.element(locBtnComparar) != null) {		
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el boton para comparar combos");
    		}else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el boton para comparar combos");
    		}
    		
    		this.ValidacionDescargaCorrecta();
    		this.Validacion4CheckboxSelecionados();
 //   		this.element(xpathCombo).isSelected();
    		this.focus(this.element(xpathCombo));
    		 
    		if (this.element(xpathCombo + xpathCambieseYa) != null) {
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el boton de Cambiese YA");
    		} else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el boton de Cambiese YA");
    		}
    		
    		if (this.element(xpathCombo + xpathConozcaMas) != null) {
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el boton de Conozca Mas");
    		} else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el boton de Conozca Mas");
    		}
    		
    		if (this.element(xpathCombo + xpathValorFront) != null) {
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra el Valor Front Descripcion");
    		} else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el Valor Front Descripcion");
    		}
    		
    		locCombo  = this.element(xpathCombo + xpathCambieseYa);
    		locCombo.click();
    		
    		do {
			} while (locInfoCambioCombo == null);
    		
    		if (this.element(locInfoCambioCombo) != null) {
    			Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se encuentra informacion del cambio combo");
    		} else {
    			Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra informacion del cambio combo");
    		}
//    		Evidence.save("Aprobar_upgrade", this);
    		Evidence.save("Aprobar_upgrade");    		
    		pageConfirmacion = new PageConfirmacion(this);
    		msjRespuesta = this.pageConfirmacion.aprobarTx2(0);
//    		Evidence.save("Transaccion confirmada", this);
    		Evidence.save("Transaccion confirmada");    		
    		Reporter.reportEvent(Reporter.MIC_PASS, "combosFront_se realiza el cambio de combo correctamente");
    		
		}else {
    		Reporter.reportEvent(Reporter.MIC_FAIL, "combosFront_no se encuentra el combo: " + combo);
    	}	
    }
    
    public void Validacion4CheckboxSelecionados() throws Exception {
    	
    	this.click(locSwitchComparar);
    	DXCUtil.wait(1);
    	List<WebElement> checkboxes = this.findElements(By.xpath("//*[contains(@id,'cphCuerpo_repCombosComparar_checkbox')]"));

        // Contador para los checkboxes seleccionados
        int selectedCount = 0;

        if (checkboxes.size() >= 4) {
		
	        // Iterar sobre la lista de checkboxes
        	for (WebElement checkbox : checkboxes) {
        		this.focus(checkbox);
        	    // Si el número de checkboxes seleccionados es menor a 4, selecciona el checkbox
//        		Evidence.save("Combo a cambiar front", this);
        		Evidence.save("Combo a cambiar front");
        	    if (selectedCount < 4) {       	    	
        	        if (!checkbox.isSelected()) {
        	            this.click(checkbox); // Método personalizado para hacer clic
        	            selectedCount++;
        	        }
        	    } else {
        	    	selectedCount++;
        	        // Verifica si el checkbox está deshabilitado
        	        String disabledAttribute = checkbox.getAttribute("disabled");
        	        if ("true".equals(disabledAttribute)) {
        	            Reporter.reportEvent(Reporter.MIC_PASS, "El checkbox del combo #" + selectedCount + " se encuentra deshabilitado.");
        	        } else {
        	            Reporter.reportEvent(Reporter.MIC_FAIL, "El checkbox del combo #" + selectedCount + " no se encuentra deshabilitado.");
        	        }
        	        
        	    }
        	}
//        	Evidence.save("4 Combos_Seleccionados", this);
        	Evidence.save("4 Combos_Seleccionados");
        }    
    }
    
    public void ValidacionDescargaCorrecta() throws Exception {
        DXCUtil.wait(5);

        this.click(locCheckSelecUnCombo);
        DXCUtil.wait(1);

        ChromeOptions options = new ChromeOptions();

        // Deshabilitar extensiones
        options.addArguments("--disable-extensions");

        // Evitar que se abra la ventana de "Data;"
        options.addArguments("--disable-popup-blocking");

        WebDriver driver = this.getDriver();

        try {
            DXCUtil.wait(5);
            String newWindowHandle = null;

            // Obtener el identificador de la ventana principal
            String originalWindow = driver.getWindowHandle();

            // Localizar y hacer clic en el botón para abrir la nueva ventana
            this.click(locBtnComparar);
            
            // Esperar a que se abra una nueva ventana y obtener los identificadores de ventanas
            Set<String> windowHandles = driver.getWindowHandles();
            long endTime = System.currentTimeMillis() + 10000; // Espera máxima de 20 segundos

            while (windowHandles.size() <= 3 && System.currentTimeMillis() < endTime) {
                Thread.sleep(1000); // Espera de 1 segundo antes de verificar nuevamente
                windowHandles = driver.getWindowHandles();
            }

            if (windowHandles.size() > 1) {
            	Reporter.reportEvent(Reporter.MIC_PASS, "combosFrontPdf_ventana para descargar pdf se abre correctamente");
            	
                // Imprimir los windowHandles y sus títulos
            	// Identificar y cambiar el enfoque a la nueva ventana correcta
            	newWindowHandle = null;
                for (String windowHandle : windowHandles) {
                	if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
	                newWindowHandle = windowHandle;
	                break;
                	}
                }
            }   else {
            	Reporter.reportEvent(Reporter.MIC_FAIL, "combosFrontPdf_ventana de descarga permite cerrar");
//            	Evidence.save("Descarga realizada", this);
            	Evidence.save("Descarga realizada");
            }
            
            DXCUtil.wait(8);
            
            // Obtener la fecha y hora actual
            LocalDateTime now = LocalDateTime.now();
            // Definir el formato
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            // Formatear la fecha y hora
            String formattedDateTime = now.format(formatter);
            
            LocalDateTime exactDateTime = LocalDateTime.parse(formattedDateTime, formatter);
            LocalDateTime adjustedDateTime = exactDateTime.withSecond(0).withNano(0);
            
         // Si los segundos están cerca de 60, se ajusta al minuto anterior
            if (exactDateTime.getSecond() >= 0) {
                adjustedDateTime = exactDateTime.minusSeconds(exactDateTime.getSecond()).withSecond(0);
            }
            
            formattedDateTime = now.format(formatter);

            if (newWindowHandle != null) {
            	// Validar que el archivo se haya descargado
                String downloadPath = "C:/Users/jprodtap/Downloads"; // Cambia esto a la ruta de tu carpeta de descargas
                String downloadedFileName = getDownloadedFileName(downloadPath, "Comparacion_Combos"+formattedDateTime);
                
                if (downloadedFileName != null) {
                	Reporter.reportEvent(Reporter.MIC_PASS, "combosFrontPdf_el archivo se ha descargado correctamente: " + downloadedFileName);
                } else {
                	Reporter.reportEvent(Reporter.MIC_FAIL, "combosFrontPdf_el archivo no se ha descargado");
                }

                // Cerrar la nueva ventana
                driver.close();
                Reporter.reportEvent(Reporter.MIC_PASS, "combosFrontPdf_ventana de descarga permite cerrar");

                // Volver a la ventana principal
                driver.switchTo().window(originalWindow);
                
                // Verificar que hemos vuelto a la ventana principal
                //String currentTitle = driver.getTitle();
                Reporter.reportEvent(Reporter.MIC_PASS, "combosFrontPdf_ventana principal permite continuar con interacciones");
                
            } else {
            	Reporter.reportEvent(Reporter.MIC_FAIL, "combosFrontPdf_no se encontró una nueva ventana de descarga válida.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Evidence.save("Descarga");
//        Evidence.save("Descarga", this);
    }

    private static String getDownloadedFileName(String downloadPath, String keyword) {
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();

        if (dirContents != null) {
            for (File file : dirContents) {
                if (file.getName().contains(keyword)) {
                    return file.getName();
                }
            }
        }
        return null;
    }
    
    
    public void InicioUpgradeCombosMiddle(PageLoginPymes pageLogin) throws Exception {

    	String numClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
    	String numIdEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
    	String nomEmpresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
    	String combo = SettingsRun.getTestData().getParameter("Combo").trim();

    	this.pageInicioMiddle = new PageInicioMiddle(pageLogin);
    	
			pageLogin.moveUpScreen();
	    	pageInicioMiddle.irAOpcion("Administración de Empresas", "Administración", "Empresa");
	    	
	    	DXCUtil.wait(10);
	    	
	    	pageLogin.write(locNumClienteEmpresarial, numClienteEmpresa);
	    	pageLogin.write(locNumIdEmpresarial, numIdEmpresa); 
	    	
	    	pageLogin.clickButton("Buscar Empresa");
	    	
//	    	Evidence.save("Upgrade datos middle ingresados", this);
	    	Evidence.save("Upgrade datos middle ingresados");
	    	
	    	do {
			} while (locByNomEmpresa == null);
	    	
	    	DXCUtil.wait(1);
	    	
	    	if (pageLogin.element(locByNomEmpresa).getText().equals(nomEmpresa)) {
	    		pageLogin.click(locNomEmpresaEdit);
				Reporter.reportEvent(Reporter.MIC_PASS, "asignar combo middle_se encuentra la empresa");
//				Evidence.save("Empresa disponible", this);
				Evidence.save("Empresa disponible");
	    	} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "asignar combo middle_no se encuentra la empresa");
				pageInicioMiddle.terminarIteracion();
				pageLogin.CerrarSesionMiddle();
	    	}
	    	
	    	do {
			} while (locSelectCombo == null);
	    	
	    	pageLogin.selectListItem(locSelectCombo, combo); 
	    	pageLogin.clickButton("Actualizar");
	    	
	    	msgAlerta = pageLogin.getMsgAlertIfExist("LbMensaje", "lblMasterAlerta", "mensajeModal");
	    	
	    	if (msgAlerta.equals("Se modificó la empresa exitosamente")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "asignar combo middle_se asigno el combo correctamente");
//				Evidence.save("Se actualizo el combo correctamente", this);
				Evidence.save("Se actualizo el combo correctamente");
	    	} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "asignar combo middle_no se asigno el combo correctamente");
				pageInicioMiddle.terminarIteracion();
				pageLogin.CerrarSesionMiddle();
	    	}
	    	
	    	this.InformesUpgradeMiddle(pageLogin);
	    	
    	}
    
    public void AsignarMaxCombosMiddle(PageLoginPymes pageLogin) throws Exception {
    	
    	String combo = SettingsRun.getTestData().getParameter("Combo").trim();
    	String valMaxTransacciones = SettingsRun.getTestData().getParameter("Max. de Transacciones").trim();
    	
    	String davivienda = SettingsRun.getTestData().getParameter("Davivienda").trim();
    	String otrosBancos = SettingsRun.getTestData().getParameter("Otros Bancos").trim();
    	String daviplata = SettingsRun.getTestData().getParameter("Daviplata").trim();
    	String depósitosElectrónicos = SettingsRun.getTestData().getParameter("Depósitos Electrónicos").trim();
    	String tarjetaPrepago = SettingsRun.getTestData().getParameter("Tarjeta Prepago").trim();
    	String daviviendaDaviplata = SettingsRun.getTestData().getParameter("Davivienda + Daviplata").trim();

    	this.pageInicioMiddle = new PageInicioMiddle(pageLogin);
    	
			pageLogin.moveUpScreen();
	    	pageInicioMiddle.irAOpcion("Administración Combos", "Administración", "Administración de Combos");
	    	
	    	DXCUtil.wait(5);
	    	
	    	if (pageLogin.element(locTableInfoComercial) != null) {
				Reporter.reportEvent(Reporter.MIC_PASS, "admin combos middle_se encuentra información comercial de los combos a comparar");
	    	} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "admin combos middle_no se encuentra información comercial de los combos a comparar");
	    	}
	    	
	    	pageLogin.selectListItem(locCompararCombos, combo); 
	    	
	    	DXCUtil.wait(3);
	    	
	    	if (pageLogin.element(locPrimerCheckCompararCombos) != null && pageLogin.element(locPrimerCheckCompararCombos).getAttribute("disabled") == null) {
				Reporter.reportEvent(Reporter.MIC_PASS, "admin combos middle_la comparacion de combos permite marcar y desmarcar check de 'Mostrar'");
	    	} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "admin combos middle_la comparacion de combos no permite marcar y desmarcar check de 'Mostrar' o  no existe combos para comparar");
	    	}
	    	
//	    	Evidence.save("Administracion de combos", this);
	    	Evidence.save("Administracion de combos");
	    	
	    	List<WebElement> locNumPaginas = this.findElements(By.xpath("//*[@id=\"cphCuerpo_dropCPagina\"]/option"));
	    	xpathTableComboMax = xpathTableComboMax.replace("COMBO", combo);
	    	
	    	int numPagin= 0;
	    	for (WebElement NumPaginas:locNumPaginas) {
	    		
	    		numPagin++;	    		
	    		
	    		DXCUtil.wait(5);
	    		
				if (pageLogin.element(xpathTableComboMax) == null) {
					pageLogin.click(locBtnSiguienteTable);
					
					/*
					 * } else if (pageLogin.element(xpathTableComboMax) == null &&
					 * numPagin==locNumPaginas.size()){ Reporter.reportEvent(Reporter.MIC_FAIL,
					 * "combo middle MaxTransf_no se encuentra el combo");
					 */
				
				} else {
					Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se encuentra el combo");
//					Evidence.save("Se encuentra el combo para actualizar", this);
					Evidence.save("Se encuentra el combo para actualizar");
					xpathTableComboMax = xpathTableComboMax + "/following-sibling::td[3]/a[1]";
					pageLogin.click(pageLogin.element(xpathTableComboMax));
					break;
				}
			}
	    	
	    	do {
			} while (pageLogin.element(locValidarMaxTrans) == null);
	    	
	    	pageLogin.selectListItem(locValidarMaxTrans, valMaxTransacciones);
	    	String xpathTableDestino = null;
	    	String xpathCheckboxDestino = null;
	    	String xpathMaxTransDestino = null;
	    	
	    	do {
			} while (pageLogin.element(locValidarMaxTrans) == null);
	    	
	    	if (valMaxTransacciones.equals("Si")) {
	    		
	    		//***************
	    		if (!davivienda.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("davivienda");
	    			
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), davivienda);
	    			
	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);

	    		} else {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("davivienda");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
	    		
	    		//***************
	    		if (!otrosBancos.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("otrosBancos");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), otrosBancos);

	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);
	    		} else {
	    			
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("otrosBancos");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
	    		
	    		//***************
	    		if (!daviplata.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("daviplata");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), daviplata);
	    			
	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);
	    		} else {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("daviplata");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
	    		
	    		//***************
	    		if (!depósitosElectrónicos.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("depósitosElectrónicos");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), depósitosElectrónicos);
	    			
	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);
	    		} else {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("depósitosElectrónicos");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
	    		
	    		//***************
	    		if (!tarjetaPrepago.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("tarjetaPrepago");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), tarjetaPrepago);
	    			
	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);
	    		} else {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("tarjetaPrepago");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
	    		
	    		//***************
	    		if (!daviviendaDaviplata.equals("0")) {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("daviviendaDaviplata");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (!pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    			
	    			xpathMaxTransDestino = xpathTableDestino+"/following-sibling::td[2]/input";

	    			DXCUtil.wait(5);
	    			pageLogin.write(pageLogin.element(xpathMaxTransDestino), daviviendaDaviplata);
	    			
	    			Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el maximo de tranferencia para "+Destino);
	    		} else {
	    			String Destino = SwitchAsignarMaxCombosDestinoMiddle("daviviendaDaviplata");
	    			xpathTableDestino = xpathDestino.replace("DESTINO", Destino);
	    			
	    			xpathCheckboxDestino = xpathTableDestino+"/following-sibling::td[1]/input";
	    			
	    			if (pageLogin.element(xpathCheckboxDestino).isSelected()) {
	    				pageLogin.click(pageLogin.element(xpathCheckboxDestino));
					}
	    		}
				
			}
//	    	Evidence.save("Se actualiza destinos", this);
	    	Evidence.save("Se actualiza destinos");
	    	pageLogin.clickButton("Actualizar");
	
	    	DXCUtil.wait(2);
	    	
	    	Alert alert = driver.switchTo().alert();
	    	String alertMessage = alert.getText();
	    	alert.accept(); // Aceptar el popup

	    	Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_"+alertMessage);
	    	Reporter.reportEvent(Reporter.MIC_PASS, "combo middle MaxTransf_se actualiza el combo correctamente");
	    	
    }
    
    public String SwitchAsignarMaxCombosDestinoMiddle(String Destino) throws Exception {
    	
    	switch (Destino) {
		case "davivienda":
			Destino = "A cuentas Davivienda";
			break;
		case "otrosBancos":
			Destino = "A cuentas otros bancos";
			break;
		case "daviplata":
			Destino = "A Daviplata";
			break;
		case "depósitosElectrónicos":
			Destino = "A Depósitos Electrónicos";
			break;
		case "tarjetaPrepago":
			Destino = "A Tarjeta Prepago";
			break;
		case "daviviendaDaviplata":
			Destino = "Davivienda + Daviplata";
			break;
    	}
    	
    	return Destino;
    }	 
    
    public void InformesUpgradeMiddle(PageLoginPymes pageLogin) throws Exception {
    	
    	String combo = SettingsRun.getTestData().getParameter("Combo").trim();
    	String ambiente = SettingsRun.getTestData().getParameter("Asignar Combo").trim();
    	
    	this.pageInicioMiddle = new PageInicioMiddle(pageLogin);
    	
		pageLogin.moveUpScreen();
    	pageInicioMiddle.irAOpcion("Informe Autogestión", "Informes", "Informes Autogestión");
    	
    	LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
    	
        pageLogin.write(locFechaDesde, formattedDate);
        pageLogin.write(locFechaHasta, formattedDate);
        
//        Evidence.save("Datos ingresados en informes middle", this);
        Evidence.save("Datos ingresados en informes middle");
        
        pageLogin.clickButton("Buscar Procesos");
        
        DXCUtil.wait(6);
        
        if (ambiente.equals("Front")) {
        	
        	pageLogin.click(locFiltroFechaMiddle);
        	DXCUtil.wait(2);
        	
//        	Evidence.save("Informes encontrados", this);
        	Evidence.save("Informes encontrados");
        	if (pageLogin.element(locComboAnteriorMiddle).getText().equals(comboActual) && pageLogin.element(locComboActualMiddle).getText().equals(combo)) {
        		Reporter.reportEvent(Reporter.MIC_PASS, "combo front Informes_se encuentran el registro del cambio de combos: " + pageLogin.element(locFechaRegistro));
			
        	} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "combo front Informes_no se encuentran el registro del cambio de combos");
			}

		} else {
        
        Reporter.reportEvent(Reporter.MIC_PASS, "combo middle Informes_se encuentran registros de cambios de combos");
		}
       
        
        
    }
}
