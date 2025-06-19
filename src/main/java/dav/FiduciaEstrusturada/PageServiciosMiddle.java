package dav.FiduciaEstrusturada;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageServiciosMiddle extends PageInicioMiddle {

	/*
	 * Constructor
	 */
	public PageServiciosMiddle(PageLoginPymes parentPage) {
		super(parentPage);
	}

	/*
	 * Variables para realizar acciones
	 */
	String[] informacionEncontrada = null;
	boolean encontrarServicio = false;
	/*
	 * Locators
	 */
	// Locators consulta empresa en servicios
	By inpNumIdentificacion = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By inpNumClienteEmpresa = By.id("cphCuerpo_txtNoClienteEmpresa");
	By btnConsultar = By.id("cphCuerpo_btnBuscar");
	By tableResultadosEmp = By.xpath("//table[@id='cphCuerpo_gvEmpresas']");

	// Locators page servicios
	By tableServicios = By.xpath("//table[@id='cphCuerpo_gvServicio']");
	By btnSiguienteRs = By.xpath("//a[text()='Siguiente >']");

	/**
	 * Ayudara a identificar que el servicio se encuentre y realiza algunas
	 * verificaciones para retornar
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] validacionServicio() throws Exception {
		this.irAOpcion(null, "Administración", "Servicios");

		String numeroIdentificacion = SettingsRun.getTestData().getParameter("Numero ID Empresa");
		String numeroClEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");

		By locators[] = { inpNumIdentificacion, inpNumClienteEmpresa };
		String data[] = { numeroIdentificacion, numeroClEmpresa };

		do {
			DXCUtil.wait(2);
		} while (this.element(btnConsultar) == null);

		enviarDataCampos(locators, data);

		WebElement btnBuscar = this.findElement(btnConsultar);
		btnBuscar.click();

		do {
			DXCUtil.wait(2);
		} while (this.element(tableResultadosEmp) == null);

		// Consulta la empresa e ingresa
		WebElement tablaResultados = this.findElement(tableResultadosEmp);
		List<WebElement> filasRs = tablaResultados.findElements(By.tagName("tr"));
		for (WebElement fila : filasRs) {
			List<WebElement> celdas = fila.findElements(By.tagName("a"));
			if (fila.getText().contains(numeroIdentificacion) && fila.getText().contains(numeroClEmpresa)) {
				for (WebElement celda : celdas) {
					celda.click();
				}
			}
		}

		do {
			DXCUtil.wait(2);
		} while (this.element(tableServicios) == null);

		do {
			WebElement tablaServicios = this.findElement(tableServicios);
			List<WebElement> filasSr = tablaServicios.findElements(By.tagName("tr"));
			for (WebElement fila : filasSr) {
				informacionEncontrada = new String[2];
				if (fila.getText().contains("Negocios fiduciarios")) {
					List<WebElement> celdas = fila.findElements(By.tagName("input"));
					for (int i = 0; i < celdas.size(); i++) {
						if (celdas.get(i).getAttribute("type").contains("checkbox") && celdas.get(i).isSelected()) {
							Reporter.write(
									"*** El servicio Negocios fiduciarios se encuentra seleccionado.");
							informacionEncontrada[i] = data[i];
							encontrarServicio = true;
						}
					}
				}
			}
			if (encontrarServicio) {
				break;
			}

			if (!encontrarServicio) {
				WebElement siguientePg = this.findElement(btnSiguienteRs);
				Reporter.write(
						"*** El servicio Negocios fiduciarios no esta seleccionado contratelo y verifique que no se repita este servicio en otro documento.");
				if (!siguientePg.getAttribute("class").contains("aspNetDisabled")) {
					Evidence.saveFullPage("Evidencia servicios", this);
					siguientePg.click();
				} else {
					Reporter.write("*** Se esperaba encontrar el servicio: Negocios fiduciarios.");
					SettingsRun.exitTestIteration();
				}
			}
		} while (encontrarServicio);

		return informacionEncontrada;
	}

	public void enviarDataCampos(By[] locators, String[] data) throws Exception {
		for (int i = 0; i < data.length; i++) {
			WebElement field = this.findElement(locators[i]);
			if (field.isDisplayed()) {
				DXCUtil.wait(1);
				field.clear();
				field.sendKeys(data[i]);
			} else {
				Reporter.write("No se encontro un campo se muestra localizador: " + field);
				SettingsRun.exitTestIteration();
			}
		}
	}

}
