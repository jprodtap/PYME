package dav.ActualizacionDeDatos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageActualizacionInformesMiddle extends PageInicioMiddle{
	
	public PageActualizacionInformesMiddle(PageLoginPymes parentPage) {
		super(parentPage);
	}
	
	// Campos y tablas con las que se va a interactuar
	By fechaInicial = By.xpath("//*[@id=\"cphCuerpo_txtFechaInicial\"]");
	By fechaFinal = By.xpath("//*[@id=\"cphCuerpo_txtFechaFinal\"]");
	By numIdentificacion = By.xpath("//*[@id=\"cphCuerpo_txtNumID\"]");
	
	By btnBuscar = By.xpath("//*[@id=\"cphCuerpo_btBuscar\"]");
	
	By tablaResultados = By.xpath("//*[@id=\"cphCuerpo_gvActividadesAdministradores\"]");
	By btnImprimir = By.xpath("//*[@id=\"paginacion\"]/div[4]/div/button[1]");
	By btnDescargar = By.xpath("//*[@id=\"paginacion\"]/div[4]/div/button[2]");
	
	String xpathNumeroIdInicial = "//*[@id=\"cphCuerpo_gvActividadesAdministradores\"]/tbody/tr[2]/td[3][contains(text(),'NUM_ID')]";
	String xpathNumeroId = null;
	String verDetalle = null;
	
	String today;
	
	//***********************************************************************************************************************
	/**
	 * Metodo inicial, ejecuta los metodos de todo el flujo de informes organizados
	 */
	public void InformeActualizacionDeDatos() throws Exception {

		String infoNumID = SettingsRun.getTestData().getParameter("Id usuario");
		Date fechaTxTemp = new Date();

		today = DXCUtil.dateToString(fechaTxTemp, "dd/mm/yyyy");
		
		// Selecciona el modulo de Actividades Administradores
		this.irAOpcion("", "Informes", "Actividades Administradores");
		DXCUtil.wait(2);
		
		this.write(fechaInicial, today);
		this.write(fechaFinal, today);
		this.write(numIdentificacion, infoNumID);
		
		Evidence.saveAllScreens("Datos para buscar en Middle", this);
		
		this.click(btnBuscar);
		
		do {
		} while (!this.isDisplayed(tablaResultados));
		
		xpathNumeroId = xpathNumeroIdInicial.replace("NUM_ID", infoNumID);
		
		if (this.isDisplayed(By.xpath("("+xpathNumeroId+")[1]"))) {
			
			verDetalle = xpathNumeroId + "//following-sibling::td[4]/a";
			this.click(By.xpath(verDetalle));
			
			DXCUtil.wait(2);

			Evidence.saveAllScreens("Informe", this);
			
		}else {
			System.out.println("Informe no encontrado");
		}
		
	}
	
}
