package dav.Autoservicio;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

public class PageConsultaUsuarioMiddle extends PageInicioMiddle {

	// Locators Middle----------------------------------
	By numClienteEmpresaMdd = By.id("cphCuerpo_txtBuscarNClienteEmpresarial");
	By tipoIdentificacionMdd = By.id("cphCuerpo_dropBuscarTipoIdentificacion");
	By numeroIdentificacionMdd = By.id("cphCuerpo_txtBuscarNumeroIdentificacion");
	By tipoAutenticacionMdd = By.id("cphCuerpo_ddlTipoAutenticacion");
	By btnBuscarMdd = By.id("cphCuerpo_btnBuscar");

	By resultadosUsuarios = By.xpath("//*[@id=\"cphCuerpo_gvUsuarios\"]/tbody");
	By btnEditarMdd = By.xpath("//*[@id='cphCuerpo_gvUsuarios']/tbody/tr/td/a[contains(text(),'Editar')]");

	public PageConsultaUsuarioMiddle(PageLoginPymes parentPage) {
		super(parentPage);
	}

	// Método para consultar que efectivamente se ha realizado la implementación a
	// la cuenta
	public void consultaMiddle() throws Exception {
		Reporter.reportEvent(Reporter.MIC_PASS, "Entro a middle correctamente.");

		this.irAOpcion(null, "Administración", "Usuarios Empresas");
		DXCUtil.wait(2);
		
		String tipoIdentificacion = SettingsRun.getTestData().getParameter("Tipo Identificación");
		String numeroDocumento = SettingsRun.getTestData().getParameter("Id usuario");
		String numeroClienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");
		String tipoAutenticacion = "Token";

		// Selección de tipo de documento
		this.selectListItem(tipoIdentificacionMdd, tipoIdentificacion);
		this.selectListItem(tipoAutenticacionMdd, tipoAutenticacion);

		By[] fieldsMdd = { numClienteEmpresaMdd, numeroIdentificacionMdd };
		String[] dataMdd = { numeroClienteEmpresa, numeroDocumento };
		
		//Ingresar información
		ingresarDatos(getDriver(), fieldsMdd, dataMdd);
		Evidence.save("Evidencias activación de token");
		Reporter.reportEvent(Reporter.MIC_INFO,
				"Se consulta la siguiente información: [ Tipo Doeumento: " + tipoAutenticacion + " - Número Documento:"
						+ numeroDocumento + " - Número de cliente empresarial. " + numeroClienteEmpresa
						+ " - Tipo de identificación: " + tipoAutenticacion);
		click(this.findElement(btnBuscarMdd));
		DXCUtil.wait(2);

		click(this.findElement(btnEditarMdd));
		DXCUtil.wait(2);
		Evidence.saveFullPage("Evidencia de Token", this);
		Reporter.reportEvent(Reporter.MIC_PASS, "Se ha tomato la evidencia de la asignación del token");

	}

	// Método que permite ingresar la información en los campos
	private static void ingresarDatos(WebDriver driver, By[] fields, String[] data) throws Exception {
		if (fields.length != data.length) {
			Reporter.reportEvent(Reporter.MIC_WARNING, "La cantidad de campos y datos no coinciden");
			return;
		}

		for (int i = 0; i < fields.length; i++) {

			WebElement field = driver.findElement(fields[i]);
			if (field.isDisplayed()) {

				field.clear();
				DXCUtil.wait(2);
				field.sendKeys(data[i]);
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontraró el campo: " + field + ", por favor verifique la información.");
				SettingsRun.exitTestIteration();
			}
		}

	}

}
