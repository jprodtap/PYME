package dav.CobrosMiddle;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageClienteEmpresaCobros extends PageInicioMiddle {

	public PageClienteEmpresaCobros(PageLoginPymes parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}
	/*
	 * Locators y elementos
	 */
	// Tabla que tiene informaci�n sobre el cliente empresarial
	By tablaClienteEmp = By.xpath("//*[@id='cphCuerpo_gvClienteEmpresarial']//tr");
	By tituloAdministracion = By.id("lblMasterTitulo");

	By labelTopeInfSaldoProm = By.id("cphCuerpo_txtCMTopeInferiorSaldoPromedio");
	By labelTopeSupSaldoProm = By.id("cphCuerpo_txtCMTopeSuperiorSaldoPromedio");
	By labelSaldoPromMesAnterior = By.id("cphCuerpo_lblCMSaldoPromedioMesAnterior");

	By indicadorDeCumplimiento = By.id("cphCuerpo_ddlCMIndicadorCumplimientoSaldoPromedio");
	
	By inpNumClienteEmpresa = By.id("cphCuerpo_txtNumeroCliente");
	By btnBuscar = By.id("cphCuerpo_btnBuscar");
	/**
	 * 
	 * @param numClienteEmp
	 * @param xpathEditar
	 * @param results
	 * @param informacion
	 * @param eventStatus
	 * @param msgReport
	 * @throws Exception
	 */
	public void consultaClienteEmpresa(WebElement numClienteEmp, String xpathEditar,
			List<HashMap<String, String>> results, HashMap<String, String> informacion, int eventStatus,
			String msgReport) throws Exception {
		
		this.irAOpcion(null, "Administración", "Cliente Empresarial");
		String clienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");
		// se obtiene encuentra el elemento
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				Evidence.save("No se encuentra el campo Cliemte empresa ");
				this.terminarIteracion();
			}

		} while (this.element(inpNumClienteEmpresa) == null);
		
		numClienteEmp = this.findElement(inpNumClienteEmpresa);
		// se consulta el cliente
		numClienteEmp.sendKeys(clienteEmpresa);
		Evidence.save("Evidencia cliente empresarial");
		WebElement elmBtnBuscar = this.findElement(btnBuscar);
		elmBtnBuscar.click();
		
		
		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				Evidence.save("campo Cliemte empresa No se presento");
				this.terminarIteracion();
			}

		} while (this.element(tablaClienteEmp) == null);

		// Se usa la variable declarada para que encuentre el bot�n editar de acuerdo al
		// n�mero de cliente empresa
		xpathEditar = "//*[@id='cphCuerpo_gvClienteEmpresarial']//td/span[contains(text(),'" + clienteEmpresa
				+ "')]/following::td/a[text()='Editar']";

		// Se verifica en la tabla si se encuentra el bot�n editar, de lo contrario se
		// cerrara la iteraci�n
		WebElement btnEditarClEmp = null;
		try {
			btnEditarClEmp = this.element(By.xpath(xpathEditar));
			Evidence.save("Evidencia cliente empresarial");
			btnEditarClEmp.click();
		} catch (NoSuchElementException e) {
			System.out.println("No se encontro el cliente empresarial");
			SettingsRun.exitTestIteration();
		}

		do {
			DXCUtil.wait(5);
		} while (this.element(tituloAdministracion) == null);

		// se obtiene la informaci�n Tope inferior saldo y Tope superior saldo
		String topeInferiorSaldoPm = this.findElement(labelTopeInfSaldoProm).getAttribute("value");
		String topeSuperiorSaldoPm = this.findElement(labelTopeSupSaldoProm).getAttribute("value");
		String saldoPromedioMesAntr = this.findElement(labelSaldoPromMesAnterior).getText();

		// se almacena la informaci�n Tope inferior saldo y Tope superior saldo se hacen
		// esos dos reemplazos para luego parsearlos a double
		informacion.put("Tope inferior saldo", topeInferiorSaldoPm.replace(".", "").replace(",", ".").trim());
		informacion.put("Tope superior saldo", topeSuperiorSaldoPm.replace(".", "").replace(",", ".").trim());
		informacion.put("Saldo Promedio Mes Anterior",
				saldoPromedioMesAntr.replace("$", "").replace(".", "").replace(",", ".").trim());

		// se obtiene la informaci�n Indicador cumplimiento Saldo Promedio
		WebElement indicCumplimientoSaldProm = this.findElement(indicadorDeCumplimiento);
		Select select = new Select(indicCumplimientoSaldProm);

		// se guarda el elemento seleccionado del elemento
		String indicadorCumplimiento = select.getFirstSelectedOption().getAttribute("value");

		// se almacena la informaci�n Indicador cumplimiento Saldo Promedio
		informacion.put("Indicador cumplimiento Saldo Promedio", indicadorCumplimiento);
		Reporter.reportEvent(eventStatus,
				"Se ha guardado la siguiente información: [ 'Indicador cumplimiento Saldo Promedio' : "
						+ indicadorCumplimiento + " ]");

	}

}
