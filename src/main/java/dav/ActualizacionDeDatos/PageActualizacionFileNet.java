package dav.ActualizacionDeDatos;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import dav.ActualizacionDeDatos.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageConfirmacion;
import dav.transversal.DatosEmpresarial;
import dav.transversal.NotificacionSMS;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;


public class PageActualizacionFileNet extends BasePageWeb {
	
	public PageActualizacionFileNet(BasePageWeb parentPage) {
		super(parentPage);
	}

	public static String NAVEGADOR = BasePageWeb.CHROME; // ESTE VALOR LO PUEDEN CAMBIAR SEGÚN SE REQUIERA	
	public static String FECHAHORA; // ESTE VALOR LO PUEDEN CAMBIAR SEGÚN SE REQUIERA	
	private static final String PORTAL = "FILENET"; // NOMBRE COMO SE CONOCE LA PÁGINA EN [DS_DaviviendaUrls.xlsx]
	private static String URL_PAGE = "http://lbdgecmwas.davivienda.loc:8080/navigator/?desktop=ConsultaGenerica"; // URL DE LA PÁGINA DE FileNet
	
	BasePageWeb pageFileNet;
	
	By locUserName = By.id("ecm_widget_layout_NavigatorMainLayout_0_LoginPane_username");
	By locPassword = By.id("ecm_widget_layout_NavigatorMainLayout_0_LoginPane_password");
	By locBtnInicioSesion = By.id("ecm_widget_layout_NavigatorMainLayout_0_LoginPane_LoginButton_label");
	
	By locTodasBusquedas = By.xpath("//*[@id=\"dijit__TreeNode_2\"]/div[1]/span[1]");
	By locConsultaNumeroIdent = By.id("dijit__TreeNode_20_label");
	By locCampoIdent = By.id("ecm_widget_search_SearchForm_0_ecm.widget.SearchCriterian_0");
	By locBtnBuscar = By.id("dijit_form_Button_1_label");
	
	By locFechaAñadido = By.id("gridx_Grid_0-7"); 
	By locFechaAñadido2 = By.id("gridx_Grid_1-7"); 
	
	By locFechaHora = By.xpath("//*[@id=\"gridx_Grid_0\"]/div[3]/div[2]/div[1]/table/tbody/tr/td[7]");
	By locFechaHora2 = By.xpath("//*[@id=\"gridx_Grid_1\"]/div[3]/div[2]/div[1]/table/tbody/tr/td[7]");
	
	By locNuevaBusqueda = By.xpath("//*[@id=\"dijit_layout_ContentPane_9\"]/div[1]/div/table/tbody/tr/td/div/div[1]/span[1]");
	
	By locImgMiniatura = By.xpath("//*[@id=\"ecm_widget_ItemPreviewPane_0\"]/div/img");
	
	By locAcciones = By.id("dijit_form_DropDownButton_6");
	By locDescargar = By.xpath("//*[@class=\"dijitReset dijitMenuItemLabel\"][(text() ='Descargar')]");
	By locDescargar2 = By.xpath("//*[@class=\"dijitReset dijitMenuItemLabel\"][(text() ='Descargar')]");
	By locDescargarPDF = By.id("dijit_MenuItem_643_text");
	By locDescargarPDF2 = By.id("dijit_MenuItem_709_text");
	
	String xpathFechaHora = "//*[@id=\"gridx_Grid_0\"]/div[3]/div[2]/div[1]/table/tbody/tr/td[7][contains(text(),'FECHAHORA')]";
	String xpathFechaHora2 = "//*[@id=\"gridx_Grid_1\"]/div[3]/div[2]/div[1]/table/tbody/tr/td[7][contains(text(),'FECHAHORA')]";
	
	String xpathFechaHoraMod = null;
	String todayHora = null;
	String msg = null;
	
	String ident = "8300010038";
	String user = "pdagonza";
	String password = "Phony2024c";
	
	
// ***********************************************************************************************************************
	/**
	 * Metodo para iniciar el portal de FileNet e indicar que se va trabajar con ese
	 */
	public void InicioFileNet() throws Exception {

		pageFileNet = new BasePageWeb(NotificacionSMS.NAVEGADOR);
		pageFileNet.maximizeBrowser();
		pageFileNet.navigate(URL_PAGE);
		
		Evidence.save("InicioFileNet");
		
		do {
		} while (!pageFileNet.isDisplayed(locUserName));
		
		pageFileNet.write(locUserName  , user);
		pageFileNet.write(locPassword  , password);
		pageFileNet.click(locBtnInicioSesion);
		
		do {
		} while (!pageFileNet.isDisplayed(locTodasBusquedas));
		
		pageFileNet.click(locTodasBusquedas);
		
		this.ConsultaFileNet();
			
	}
	
// ***********************************************************************************************************************
	/**
	 * Metodo para iniciar el portal de FileNet y consultar el informe deseado
	 */	
	public void ConsultaFileNet() throws Exception {
		
		do {
		} while (!pageFileNet.isDisplayed(locConsultaNumeroIdent));
		
		pageFileNet.click(locConsultaNumeroIdent);
		
		do {
		} while (!pageFileNet.isDisplayed(locCampoIdent));
		
		pageFileNet.write(locCampoIdent, ident);
		pageFileNet.click(locBtnBuscar);
		
	}
	
	public String EncontrarArchivo(boolean segundaVez) throws Exception {
		
		if (segundaVez) {
			
			pageFileNet.click(locNuevaBusqueda);
			
			do {
			} while (!pageFileNet.isDisplayed(locCampoIdent));
			
			pageFileNet.write(locCampoIdent, ident);
			pageFileNet.click(locBtnBuscar);
			
			do {
			} while (!pageFileNet.isDisplayed(locFechaAñadido2));
			
			pageFileNet.click(locFechaAñadido2);
			DXCUtil.wait(1);
			pageFileNet.click(locFechaAñadido2);
			
			Evidence.saveAllScreens("Segunda busqueda FileNet", pageFileNet);
			
			do {
			} while (!pageFileNet.isDisplayed(locFechaHora2));
			
			xpathFechaHoraMod = xpathFechaHora2.replace("FECHAHORA", FECHAHORA);
			
			System.out.println(FECHAHORA);
			
			for (int i = 1; i < 5; i++) {
				
				if (!pageFileNet.isDisplayed(pageFileNet.element(xpathFechaHoraMod))) {
					
					Date fecha = new Date();
					Date fechaToday = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, 0);
					Date hora1 = DXCUtil.dateAdd(fecha, Calendar.MINUTE, i-2);
					
					String today = DXCUtil.dateToString(fechaToday, "D-M-YY");
					String hora = DXCUtil.hourToString(hora1, "HH:mm");
		            todayHora = today + " "+ hora;
					todayHora = todayHora.replace("-", "/").replace("24 ", "2024 ");
					
					xpathFechaHoraMod = xpathFechaHora2.replace("FECHAHORA", todayHora);
					
					System.out.println("-" + xpathFechaHoraMod);
					
					if (!pageFileNet.isDisplayed(pageFileNet.element(xpathFechaHoraMod)) && i == 4) {
						msg = "No encontrado";
					}
					
				} else {
					this.ValidacionesArchivo(xpathFechaHoraMod, segundaVez);
					msg = "Encontrado";
				}
			}
			
		} else {
		
			do {
			} while (!pageFileNet.isDisplayed(locFechaAñadido));
			
			pageFileNet.click(locFechaAñadido);
			DXCUtil.wait(1);
			pageFileNet.click(locFechaAñadido);
			
			Evidence.saveAllScreens("Primera busqueda FileNet", pageFileNet);
			
			do {
			} while (!pageFileNet.isDisplayed(locFechaHora));
			
			xpathFechaHoraMod = xpathFechaHora.replace("FECHAHORA", FECHAHORA);
			
			System.out.println(FECHAHORA);
			
			for (int i = 1; i < 5; i++) {
				
				if (!pageFileNet.isDisplayed(pageFileNet.element(xpathFechaHoraMod))) {
					
					Date fecha = new Date();
					Date fechaToday = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, 0);
					Date hora1 = DXCUtil.dateAdd(fecha, Calendar.MINUTE, i-2);
					
					String today = DXCUtil.dateToString(fechaToday, "D-M-YY");
					String hora = DXCUtil.hourToString(hora1, "HH:mm");
		            todayHora = today + " "+ hora;
					todayHora = todayHora.replace("-", "/").replace("24 ", "2024 ");
					
					xpathFechaHoraMod = xpathFechaHora.replace("FECHAHORA", todayHora);
					
					System.out.println("-" + xpathFechaHoraMod);
					
					if (!pageFileNet.isDisplayed(pageFileNet.element(xpathFechaHoraMod)) && i == 4) {
						msg = "No encontrado";
					}
					
				} else {
					
					this.ValidacionesArchivo(xpathFechaHoraMod, segundaVez);
					msg = "Encontrado";
					break;
				}
			}
			
		}
		
		return msg;
	}
	
// ***********************************************************************************************************************
	/**
	 * Metodo para validar las caracteristicas archivo encontrado en FileN
	 */
	public void ValidacionesArchivo (String xpathFecha, boolean segundaVez) throws Exception {
	
		pageFileNet.click(pageFileNet.element(xpathFecha));
		DXCUtil.wait(3);
		
		if (pageFileNet.isDisplayed(locImgMiniatura)) {
			Reporter.reportEvent(Reporter.MIC_PASS,"Se encuentra imagen miniatura del PDF");
			Evidence.saveAllScreens("Imagen miniatura", pageFileNet);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,"No se encuentra imagen miniatura del PDF");
		}
		
		do {
		} while (!pageFileNet.isDisplayed(locAcciones));
		
		pageFileNet.click(locAcciones);
		
		if (segundaVez) {
			
			do {
			} while (!pageFileNet.isDisplayed(locDescargar2));
			
			pageFileNet.click(locDescargar2);
			
			if (pageFileNet.isDisplayed(locDescargar2)) {
				pageFileNet.click(locDescargar2);
				Reporter.reportEvent(Reporter.MIC_PASS,"Se encuentra la opcion descargar del PDF");
				Evidence.saveAllScreens("Opcion descargar", pageFileNet);
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,"No se encuentra la opcion descargar del PDF");
			}	
			
			
		} else {
			
			do {
			} while (!pageFileNet.isDisplayed(locDescargar));
			
			pageFileNet.click(locDescargar);
			
			if (pageFileNet.isDisplayed(locDescargar)) {
				pageFileNet.click(locDescargar);
				Reporter.reportEvent(Reporter.MIC_PASS,"Se encuentra la opcion descargar del PDF");
				Evidence.saveAllScreens("Opcion descargar", pageFileNet);
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,"No se encuentra la opcion descargar del PDF");
			}		
			
		}

		xpathFecha = xpathFecha + "/preceding-sibling::td[4]";
		
		try {
			
			pageFileNet.click(pageFileNet.element(xpathFecha));
			
			List<String> idsWind = pageFileNet.getIdWindows();
	        String portalWind = idsWind.get(idsWind.size() - 1);// RECUPERA LA VENTANA ACTUAL CON LA QUE SE VA INTERATUAR
	        pageFileNet.changeWindow(portalWind);// VENTANA ACTUAL CON LA QUE SE VA INTERATUAR
	        
			Reporter.reportEvent(Reporter.MIC_PASS,"Abre la ventana del archivo PDF");
			DXCUtil.wait(5);
			Evidence.saveAllScreens("Ventana emergente del archivo", pageFileNet);
			
		} catch (Exception e) {
			Reporter.reportEvent(Reporter.MIC_FAIL,"No abre la ventana del archivo PDF");
		}
		
	}
}
