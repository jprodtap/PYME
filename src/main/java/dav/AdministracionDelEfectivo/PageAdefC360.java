package dav.AdministracionDelEfectivo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import dav.ActualizacionDeDatos.PageActualizacionDeDatos;

import dav.middlePymes.PageInicioMiddle;
import dav.AdministracionDelEfectivo.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.*;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import dav.c360.PageInicioC360;
import dav.c360.PageLogin;

public class PageAdefC360 extends BasePageWeb {
	
	public PageAdefC360(BasePageWeb parentPage) {
		super(parentPage);
	}
	
	PageLogin c360 = null;
	PageInicioC360 pageInicio = null;
	
	By locRazonSocial = By.xpath("//input[@id=\"cphCuerpo_txtNombreEmpresa\"]");
	By locDireccActual = By.xpath("//input[@id=\"cphCuerpo_txtDireccionActual\"]");
	By locTelActual = By.xpath("//input[@id=\"cphCuerpo_txtCelular\"]");
	
	String razonSocial;
	String direccActual;
	String telActual;
		
	By locBienvenidoMainPage = By.xpath("//div[contains(text() , 'Bienvenido')]");
	String xpathNumIdent = "//a[@name=\"DAV_Identification Number\"][contains(text(), 'NUM_IDENT')]";
	
	By locRazonSocial360 = By.xpath("//input[@placeholder=\"Razón social\"]");
	By locDireccActual360 = By.xpath("//input[@placeholder=\"Dirección principal\"]");
	By locTelActual360 = By.xpath("//input[@placeholder=\"Celular Principal\"]");
	
	String razonSocial360;
	String direccActual360;
	String telActual360;

	/**
	 * Extrae los datos de cliente empresarial en Portal Front para despues comparar en C360
	 */
	public void ExtraerDatosFront() throws Exception {
		
		do {
		} while (this.element(locRazonSocial) == null);
		
		razonSocial = this.element(locRazonSocial).getAttribute("value");
		direccActual = this.element(locDireccActual).getAttribute("value");
		telActual = this.element(locTelActual).getAttribute("value");
		
		Evidence.save("Datos Front C360");
		
	}
	
	/**
	 * Inicializa C360 y busca el cliente empresarial
	 */
	public void InicioC360() throws Exception {
	
	String id = SettingsRun.getTestData().getParameter("Usuario C360");
	String contraseña = SettingsRun.getTestData().getParameter("Contraseña C360");
	
		System.out.println("Usuario: " + id + " - Contraseña:" + contraseña);
		Reporter.write("*** Transando en portal c360");
		String navegador = BasePageWeb.CHROME;
		try {
	
			//Inicialoiza cliente 360
			this.c360 = new PageLogin(navegador);
			this.c360.maximizeBrowser();
			this.pageInicio = new PageInicioC360(c360);
			pageInicio.refresh();
			this.c360.login360(id, contraseña);
	
			
			//Valida que el inicio de sesion sea exitoso
			do {
				DXCUtil.wait(3);
				pageInicio.reporteAlertas();
			} while (pageInicio.element(locBienvenidoMainPage) == null);
			
			Evidence.save("Login C360");
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Extrae los datos de cliente empresarial en C360 para despues comparar con los del Portal Pyme
	 */
	public void ExtraerDatosC360() throws Exception {
		
		String numIdentificacion = SettingsRun.getTestData().getParameter("Numero ID Empresa");
		
		pageInicio.irAModulo(PageInicioC360.MOD_EMPRESAS);
		
		String existe= pageInicio.buscarExistenciaId(numIdentificacion, "Persona Juridica");
		
		if(existe!=null) {
			
			//Xpath que busca el numero de ID de la empresa
			xpathNumIdent = xpathNumIdent.replace("NUM_IDENT", numIdentificacion);
			
			DXCUtil.wait(5);

			//Si encuentra el numero de ID lo selecciona, de lo contrario reporta el fallo y continua
			if (pageInicio.element(xpathNumIdent) != null) {
				this.click(pageInicio.element(xpathNumIdent));
				DXCUtil.wait(1);
				pageInicio.reporteAlertas();
				
				do {
				} while (pageInicio.element(locRazonSocial360) == null);
				
				DXCUtil.wait(3);
				Evidence.save("Datos C360 Front");
				
				razonSocial360 = pageInicio.element(locRazonSocial360).getAttribute("value");
				direccActual360 = pageInicio.element(locDireccActual360).getAttribute("value");
				telActual360 = pageInicio.element(locTelActual360).getAttribute("value");
				
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "c360_No se encontro numero de documento");
				SettingsRun.exitTestIteration();				
			}
			
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "c360_No se encontro numero de documento");
			SettingsRun.exitTestIteration();	
		}
	}

	/**
	 * Compara los datos de Portal front vs C360
	 */
	public void CompararDatosFront360() throws Exception {
		
		if (razonSocial.equals(razonSocial360)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_Coincide en nombre de razon social");
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_No coincide en nombre de razon social");
		}
		
		if (direccActual.equals(direccActual360)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_Coincide en direccion actual");
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_No coincide en direccion actual");
		}
		
		if (telActual.equals(telActual360)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_Coincide en telefono actual");
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "c360_No coincide en telefono actual");
		}
		
		pageInicio.reporteAlertas();
		
		Evidence.save("Comparacion de datos");
		
		DXCUtil.wait(1);
		c360.cerrarSesion();
		c360.closeCurrentBrowser();
		
	}

}
