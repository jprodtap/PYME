package dav.ActualizacionDeDatos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageActualizacionCalendario extends BasePageWeb {
	
	public PageActualizacionCalendario(BasePageWeb parentPage) {
		super(parentPage);
	}
	
	String[] partesFecha = null;
	
// ***********************************************************************************************************************
	/**
	 * Metodo para seleccionar la fecha de vinculacion del socio en el calendario virtual del portal
	 */
	public void SeleccionarCalendarioVinculacionSocio(String fecha) throws Exception {
		
		partesFecha = new String[3]; 
		
		partesFecha = fecha.split("/");
		
		String dia = partesFecha[0];
        String mes = partesFecha[1];
        String anio = partesFecha[2];
		
		// Mapear los nombres abreviados de los meses a sus equivalentes numéricos
        Map<String, String> mesesAbreviados = new HashMap<>();
        mesesAbreviados.put("01", "ene");
        mesesAbreviados.put("02", "feb");
        mesesAbreviados.put("03", "mar");
        mesesAbreviados.put("04", "abr");
        mesesAbreviados.put("05", "may");
        mesesAbreviados.put("06", "jun");
        mesesAbreviados.put("07", "jul");
        mesesAbreviados.put("08", "ago");
        mesesAbreviados.put("09", "sep");
        mesesAbreviados.put("10", "oct");
        mesesAbreviados.put("11", "nov");
        mesesAbreviados.put("12", "dic");

        // Obtener el mes en formato numérico
        String mesTexto = mesesAbreviados.get(mes);
		
		WebElement botonAbrirCalendario = this.findElement(By.xpath("//*[@id=\"cphCuerpo_btnCalentario1\"]"));
		botonAbrirCalendario.click();
		
		DXCUtil.wait(1);
		
		WebElement botonCambiarAños = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_title\"]"));
		
        botonCambiarAños.click();
        DXCUtil.wait(1);
        botonCambiarAños.click();
        
        DXCUtil.wait(1);
        
        // Encuentra el botón para retroceder 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonRetroceder12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_prevArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_year_0_0\"]")).getAttribute("textContent")) > Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonRetroceder12Años.click();
        }
        
        // Encuentra el botón para avanzar 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonAvanzar12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_nextArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_year_2_3\"]")).getAttribute("textContent")) < Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonAvanzar12Años.click();
        }
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el año deseado
        WebElement añoDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'"+anio+"')]//*"));
        añoDeseado.click();
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el mes deseado
        WebElement mesDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_monthsBody\"]//td[contains(.,'"+mesTexto+"')]"));
        mesDeseado.click();
        
        DXCUtil.wait(1);
        
        if (mesTexto.equals("mar"))
        	mesTexto = "marzo";
        	
        if (dia.startsWith("0"))
        	dia = dia.replace("0", "");
        
        // Encuentra y haz clic en el día deseado
        WebElement díaDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_daysBody\"]//td/div[contains(@title,'"+mesTexto+"') and (text() = '"+dia+"')]"));
        díaDeseado.click();
        
    }

// ***********************************************************************************************************************
	/**
	 * Metodo para seleccionar la fecha de desvinculacion del socio en el calendario virtual del portal
	 */
	public void SeleccionarCalendarioDesVinculacionSocio(String fecha) throws Exception {
		
		partesFecha = new String[3]; 
		
		partesFecha = fecha.split("/");
		
		String dia = partesFecha[0];
        String mes = partesFecha[1];
        String anio = partesFecha[2];
		
		// Mapear los nombres abreviados de los meses a sus equivalentes numéricos
        Map<String, String> mesesAbreviados = new HashMap<>();
        mesesAbreviados.put("01", "ene");
        mesesAbreviados.put("02", "feb");
        mesesAbreviados.put("03", "mar");
        mesesAbreviados.put("04", "abr");
        mesesAbreviados.put("05", "may");
        mesesAbreviados.put("06", "jun");
        mesesAbreviados.put("07", "jul");
        mesesAbreviados.put("08", "ago");
        mesesAbreviados.put("09", "sep");
        mesesAbreviados.put("10", "oct");
        mesesAbreviados.put("11", "nov");
        mesesAbreviados.put("12", "dic");

        // Obtener el mes en formato numérico
        String mesTexto = mesesAbreviados.get(mes);
		
		WebElement botonAbrirCalendario = this.findElement(By.xpath("//*[@id=\"cphCuerpo_btnCalentario2\"]"));
		botonAbrirCalendario.click();
		
		DXCUtil.wait(1);
		
		WebElement botonCambiarAños = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_title\"]"));
		
        botonCambiarAños.click();
        DXCUtil.wait(1);
        botonCambiarAños.click();
        
        DXCUtil.wait(1);
        
        // Encuentra el botón para retroceder 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonRetroceder12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_prevArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_year_0_0\"]")).getAttribute("textContent")) > Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonRetroceder12Años.click();
        }
        
        // Encuentra el botón para avanzar 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonAvanzar12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_nextArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_year_2_3\"]")).getAttribute("textContent")) < Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonAvanzar12Años.click();
        }
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el año deseado
        WebElement añoDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_yearsBody\"]//td[contains(.,'"+anio+"')]//*"));
        añoDeseado.click();
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el mes deseado
        WebElement mesDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_monthsBody\"]//td[contains(.,'"+mesTexto+"')]"));
        mesDeseado.click();
        
        DXCUtil.wait(1);
        
        if (mesTexto.equals("mar"))
        	mesTexto = "marzo";
        	
        if (dia.startsWith("0"))
        	dia = dia.replace("0", "");
        
        // Encuentra y haz clic en el día deseado
        WebElement díaDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaDesVinculacion_daysBody\"]//td/div[contains(@title,'"+mesTexto+"') and (text() = '"+dia+"')]"));
        díaDeseado.click();
        
    }
		
// ***********************************************************************************************************************
	/**
	 * Metodo para seleccionar la fecha de vinculacion del representante en el calendario virtual del portal
	 */
	public void SeleccionarCalendarioVinculacionRepresentante(String fecha) throws Exception {
		
		partesFecha = new String[3]; 
		
		partesFecha = fecha.split("/");
		
		String dia = partesFecha[0];
        String mes = partesFecha[1];
        String anio = partesFecha[2];
		
		// Mapear los nombres abreviados de los meses a sus equivalentes numéricos
        Map<String, String> mesesAbreviados = new HashMap<>();
        mesesAbreviados.put("01", "ene");
        mesesAbreviados.put("02", "feb");
        mesesAbreviados.put("03", "mar");
        mesesAbreviados.put("04", "abr");
        mesesAbreviados.put("05", "may");
        mesesAbreviados.put("06", "jun");
        mesesAbreviados.put("07", "jul");
        mesesAbreviados.put("08", "ago");
        mesesAbreviados.put("09", "sep");
        mesesAbreviados.put("10", "oct");
        mesesAbreviados.put("11", "nov");
        mesesAbreviados.put("12", "dic");

        // Obtener el mes en formato numérico
        String mesTexto = mesesAbreviados.get(mes);
		
		WebElement botonAbrirCalendario = this.findElement(By.xpath("//*[@id=\"cphCuerpo_Button1\"]"));
		botonAbrirCalendario.click();
		
		DXCUtil.wait(1);
		
		WebElement botonCambiarAños = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_title\"]"));
		
        botonCambiarAños.click();
        DXCUtil.wait(1);
        botonCambiarAños.click();
        
        DXCUtil.wait(1);
        
        // Encuentra el botón para retroceder 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonRetroceder12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_prevArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_year_0_0\"]")).getAttribute("textContent")) > Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonRetroceder12Años.click();
        }
        
        // Encuentra el botón para avanzar 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonAvanzar12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_nextArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_year_2_3\"]")).getAttribute("textContent")) < Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonAvanzar12Años.click();
        }
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el año deseado
        WebElement añoDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_yearsBody\"]//td[contains(.,'"+anio+"')]//*"));
        añoDeseado.click();
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el mes deseado
        WebElement mesDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_monthsTable\"]//td[contains(.,'"+mesTexto+"')]"));
        mesDeseado.click();
        
        DXCUtil.wait(1);
        
        if (mesTexto.equals("mar"))
        	mesTexto = "marzo";
        	
        if (dia.startsWith("0"))
        	dia = dia.replace("0", "");
        
        // Encuentra y haz clic en el día deseado
        WebElement díaDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender1_daysBody\"]//td/div[contains(@title,'"+mesTexto+"') and (text() = '"+dia+"')]"));
        díaDeseado.click();
        
    }

// ***********************************************************************************************************************
	/**
	 * Metodo para seleccionar la fecha de desvinculacion del representante en el calendario virtual del portal
	 */
	public void SeleccionarCalendarioDesVinculacionRepresentante(String fecha) throws Exception {
		
		partesFecha = new String[3]; 
		
		partesFecha = fecha.split("/");
		
		String dia = partesFecha[0];
        String mes = partesFecha[1];
        String anio = partesFecha[2];
		
		// Mapear los nombres abreviados de los meses a sus equivalentes numéricos
        Map<String, String> mesesAbreviados = new HashMap<>();
        mesesAbreviados.put("01", "ene");
        mesesAbreviados.put("02", "feb");
        mesesAbreviados.put("03", "mar");
        mesesAbreviados.put("04", "abr");
        mesesAbreviados.put("05", "may");
        mesesAbreviados.put("06", "jun");
        mesesAbreviados.put("07", "jul");
        mesesAbreviados.put("08", "ago");
        mesesAbreviados.put("09", "sep");
        mesesAbreviados.put("10", "oct");
        mesesAbreviados.put("11", "nov");
        mesesAbreviados.put("12", "dic");

        // Obtener el mes en formato numérico
        String mesTexto = mesesAbreviados.get(mes);
		
		WebElement botonAbrirCalendario = this.findElement(By.xpath("//*[@id=\"cphCuerpo_Button2\"]"));
		botonAbrirCalendario.click();
		
		DXCUtil.wait(1);
		
		WebElement botonCambiarAños = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_title\"]"));
		
        botonCambiarAños.click();
        DXCUtil.wait(1);
        botonCambiarAños.click();
        
        DXCUtil.wait(1);
        
        // Encuentra el botón para retroceder 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonRetroceder12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_prevArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_year_0_0\"]")).getAttribute("textContent")) > Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonRetroceder12Años.click();
        }
        
        // Encuentra el botón para avanzar 12 años y haz clic en él hasta llegar al año deseado
        WebElement botonAvanzar12Años = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_nextArrow\"]"));
        while (Integer.parseInt(this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_year_2_3\"]")).getAttribute("textContent")) < Integer.parseInt(anio)) {
//        	if (this.isDisplayed(this.findElement(By.xpath("//*[@id=\"cphCuerpo_FiltrotxtSocioFechaVinculacion_yearsBody\"]//td[contains(.,'1950')]")))) {
//				break;
//			}
            botonAvanzar12Años.click();
        }
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el año deseado
        WebElement añoDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_yearsBody\"]//td[contains(.,'"+anio+"')]//*"));
        añoDeseado.click();
        
        DXCUtil.wait(1); 
        
        // Encuentra y haz clic en el mes deseado
        WebElement mesDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_monthsTable\"]//td[contains(.,'"+mesTexto+"')]"));
        mesDeseado.click();
        
        DXCUtil.wait(1);
        
        if (mesTexto.equals("mar"))
        	mesTexto = "marzo";
        	
        if (dia.startsWith("0"))
        	dia = dia.replace("0", "");
        
        // Encuentra y haz clic en el día deseado
        WebElement díaDeseado = this.findElement(By.xpath("//*[@id=\"cphCuerpo_CalendarExtender2_daysBody\"]//td/div[contains(@title,'"+mesTexto+"') and (text() = '"+dia+"')]"));
        díaDeseado.click();
        
    }

}
