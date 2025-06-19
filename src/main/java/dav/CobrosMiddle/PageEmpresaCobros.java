package dav.CobrosMiddle;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageEmpresaCobros extends PageInicioMiddle {

	public PageEmpresaCobros(PageLoginPymes parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Locators y elementos
	 */
	// Tabla resultados empresa
	By tablaEmpresaResultados = By.xpath("//*[@id='cphCuerpo_gvEmpresa']");
	By tituloAdministracion = By.id("lblMasterTitulo");

	// Tabla donde se tomara el conteo de transacciones acumuladas
	By tablaCantAcumulTx = By.xpath("//table[@id='cphCuerpo_gvConteoServicios']");

	By inpNumClienteEmpresa = By.id("cphCuerpo_txtBQNumeroClienteEmpresarial");
	By inpIdEmpresa = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By btnConsultar = By.id("cphCuerpo_btnQBBuscar");

	By labelConteoTx = By.id("cphCuerpo_gvConteoServicios");

	By labelSegmento = By.id("cphCuerpo_lblDESegmento");
	By labelTotalTxAcumuladas = By.id("cphCuerpo_lblDECantidadTransaccionesAcumuladas");
	By labelTxPorEmpresa = By.id("cphCuerpo_lblDECantidadTransaccionesEmpresa");

	By btnGrabar = By.id("cphCuerpo_btnGrabar");

	By alertError = By.id("lblMasterAlerta");

	// variables inicializadas
	int[] contadorActualizado = null;

	/**
	 * Consulta de la empresa
	 * 
	 * @throws Exception
	 */
	public void consultarEmpresa() throws Exception {
		this.irAOpcion(null, "Administración", "Empresa");
		Evidence.save("Evidencia Final");
		String clienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");
		String nitEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa");

		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(inpNumClienteEmpresa) == null);
		
		Evidence.save("Evidencia empresa");
		// Se consulta por n�mero de cliente y nit
		WebElement numClienteEmp = this.findElement(inpNumClienteEmpresa);
		WebElement numIdEmpresa = this.findElement(inpIdEmpresa);

		numClienteEmp.sendKeys(clienteEmpresa);
		numIdEmpresa.sendKeys(nitEmpresa);

		WebElement btnBuscar = this.findElement(btnConsultar);
		btnBuscar.click();
		Evidence.save("Evidencia empresa");
		
		 contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >=30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
				SettingsRun.exitTestIteration();
			}
		} while (this.element(tablaEmpresaResultados) == null);

		// Este xpath se arma para darle click a editar
		Evidence.save("Evidencia empresa");
		String xpathEditarEmp = "//*[@id='cphCuerpo_gvEmpresa']//td[contains(text(),'" + clienteEmpresa
				+ "')]/following-sibling::td[contains(text(),'" + nitEmpresa + "')]/following-sibling::td/a";

		WebElement btnEditar = this.findElement(By.xpath(xpathEditarEmp));
		DXCUtil.wait(5);
		if (btnEditar != null) {
			btnEditar.click();
		} else {
			System.out.println("Se esperaca encontrar resultados que coincidieran con la información.");
			Evidence.save("Evidencia empresa");
			SettingsRun.exitTestIteration();
		}
		Evidence.save("Evidencia empresa");

		do {
			DXCUtil.wait(2);
		} while (this.element(labelConteoTx) == null);
	}

	/**
	 * Nos ayuda a comparar los
	 * 
	 * @throws Exception
	 */
	public void comparacionComboEmpresa(int eventStatus, String msgReport, String comboEncontrado, String comboInicial,
			List<HashMap<String, String>> results, HashMap<String, String> informacion) throws Exception {

		if (comboEncontrado.equals(comboInicial)) {
			eventStatus = Reporter.MIC_PASS;
			msgReport = "El combo '" + comboEncontrado + "' encontrado coinside correctamente con la data entregada.";
			Reporter.reportEvent(eventStatus, msgReport);
		} else {
			eventStatus = Reporter.MIC_WARNING;
			msgReport = "El combo '" + comboEncontrado
					+ "' encontrado no coinside con la data entregada, pero cuenta con combo por lo tanto se consultara y se verificara.";
			Reporter.reportEvent(eventStatus, msgReport);
		}
		comboInicial = comboEncontrado;

		DXCUtil.wait(2);
		// se obtiene el segmento que se eucuentra visible
		String segmento = this.findElement(labelSegmento).getText();
		String cantidadTotalTx = this.findElement(labelTotalTxAcumuladas).getText();
		// Se adiciona el segmento encontrado
		informacion.put("Cantidad Tx Acumuladas", cantidadTotalTx);
		informacion.put("Segmento", segmento);
		Evidence.saveFullPage("Evidencia empresa", this);
		eventStatus = Reporter.MIC_INFO;
		Reporter.reportEvent(eventStatus, "Se ha guardado la siguiente información: [ 'Segmento' : " + segmento + " ]");
		// se obtiene la cantidad Cantidad Maxima Tx Empresa de pantalla administraci�n
		// empresa
		String cantidadMaxTxEmp = this.findElement(labelTxPorEmpresa).getText();

		WebElement elmBtnGrabar = this.findElement(btnGrabar);
		elmBtnGrabar.click();

		do {
			DXCUtil.wait(1);
		} while (this.element(alertError) == null);

		// se extrae informaci�n guardada de administraci�n de combos
		String cantiMaxTxEmpCombos = informacion.get("Cantidad Maxima Tx Empresa");
		DXCUtil.wait(3);
		// se comparan los dos datos obtenidos anteriormente
		if (Integer.parseInt(cantidadMaxTxEmp) == Integer.parseInt(cantiMaxTxEmpCombos)) {

			// Este array de string contiene los destinos
			String[] destinos = { informacion.get("Destino A cuentas Davivienda"),
					informacion.get("Destino A cuentas otros bancos"), informacion.get("Destino A Daviplata"),
					informacion.get("Destino A Depósitos Electrónicos"), informacion.get("Destino A Tarjeta Prepago"),
					informacion.get("Destino Davivienda + Daviplata") };
			// este array de string contiene la cantidad de tx para cada destino
			String[] cantidadDestino = { informacion.get("A cuentas Davivienda"),
					informacion.get("A cuentas otros bancos"), informacion.get("A Daviplata"),
					informacion.get("A Depósitos Electrónicos"), informacion.get("A Tarjeta Prepago"),
					informacion.get("Davivienda + Daviplata") };

			WebElement cantMxTxRowTable = null;

			// los array deben tener la misma cantidad de datos, en este caso contaremos la
			// cantidad de destinos
			// destinos.length
			WebElement conteoTxTabla = this.findElement(tablaCantAcumulTx);
			List<WebElement> filas = conteoTxTabla.findElements(By.tagName("tr"));
			for (int i = 1; i < filas.size() - 1; i++) {

				// Se arma un xpath incluyendo la información que va a iterar
				String xpathComparar = "//*[@id='cphCuerpo_gvConteoServicios']//td[contains(text(),'" + destinos[i] + "')]/following-sibling::td[contains(text(),'" + cantidadDestino[i] + "')]";
				WebElement celda = filas.get(i).findElements(By.tagName("td")).get(2);
				String contador = celda.getText();
				informacion.put("Tx acumuladas " + i, contador);
				cantMxTxRowTable = this.element(By.xpath(xpathComparar));
				// si el elemento es diferente a nulo imprime el reporte
				if (cantMxTxRowTable == null) {
					// si el xpath es nulo es porque la informaci�n no coincide
					eventStatus = Reporter.MIC_FAIL;
					Reporter.reportEvent(eventStatus,"Se esperaba que los datos de Administración de combos y empresa coincidieran.");
					SettingsRun.exitTestIteration();
				}
			}
			eventStatus = Reporter.MIC_PASS;
			Reporter.reportEvent(eventStatus, "Los datos coinciden con Administración Combos.");

		} else {
			// Si no coincide esto se considera como defecto
			eventStatus = Reporter.MIC_FAIL;
			Reporter.reportEvent(eventStatus,
					"Se esperaba que la Cantidad Máxima Transacciones Empresa fuera igual en administración combos y en empresa.");
			SettingsRun.exitTestIteration();

		}
	}

	public String comboSeleccionado() throws Exception {
		/*
		 * validar datos guardados de administración de combos contra empresa
		 */
		// Este elemento ingresa a administración de empresa
		this.consultarEmpresa();

		Evidence.saveFullPage("Evidencia empresa", this);
		
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				Evidence.save("No se encuentra el titulo de Administracion");
				this.terminarIteracion();
			}

		} while (this.element(tituloAdministracion) == null);

		// una vez ingresa se selecciona el combo que tiene parametrizado
		// Se busca el elemento que tiene el combo seleccionado
		WebElement comboServicios = this.findElement(By.id("cphCuerpo_ddlDEComboServicios"));
		Select select = new Select(comboServicios);
		// se guarda el elemento seleccionado del elemento en un string
		String comboEncontrado = select.getFirstSelectedOption().getText();

		return comboEncontrado;

	}

	/**
	 * Compara el contador de transacciones con combos del despu�s de la transacci�n
	 * 
	 * @return
	 * @throws Exception
	 */
	public void validarContadorPorTx() throws Exception {
		contadorActualizado = new int[5];
		Evidence.save("Evidencia Empresa");
		WebElement conteoTxTabla = this.findElement(tablaCantAcumulTx);
		List<WebElement> filas = conteoTxTabla.findElements(By.tagName("tr"));
		for (int i = 1; i < filas.size() - 1; i++) {
			try {
				WebElement celda = filas.get(i).findElements(By.tagName("td")).get(2);
				int contador = Integer.parseInt(celda.getText());
				contadorActualizado[i - 1] = contador;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		Evidence.save("Evidencia empresa");

	}

	/**
	 * Se mantiene alacenada la informaci�n sobre el conteo
	 * 
	 * @return
	 */
	public int[] getContadorAct() {
		return contadorActualizado;
	}
}
