package dav.CobrosMiddle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dav.middlePymes.PageInicioMiddle;
import dav.middlePymes.PageServicios;
import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class ControllerMiddleCobros extends PageInicioMiddle {
	PageEmpresaCobros pageEmpresaCobros;

	public ControllerMiddleCobros(PageLoginPymes parentPage) {
		super(parentPage);
		this.pageEmpresaCobros = new PageEmpresaCobros(parentPage);
	}

	/*
	 * Pages
	 */
	PageAdminCombosCobros pageCombosCobros = null;

	List<HashMap<String, String>> results = new ArrayList<>();

	/**
	 * Controla la información que se va a retornar
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> consultarCombos() throws Exception {
		this.pageCombosCobros = new PageAdminCombosCobros(this);

		String combo = SettingsRun.getTestData().getParameter("Combos");
		String tipoCobro = SettingsRun.getTestData().getParameter("Tipo Prueba Cobros");
		// Se da cambia el texto de servicio
		String servicioExcel = SettingsRun.getTestData().getParameter("Servicio");
		String servicio = PageServicios.getNbLinkServicio(servicioExcel);

		int eventStatus = Reporter.MIC_PASS;
		String msgReport = "";

		// Si el caso de prueba es sin combos es porque el campo combo esta vacio en el
		// excel
		// ------------------------------------hacer validaciones (que pasa si la prueba
		// es con combos pero el campo va vacio)
		if (tipoCobro.equals("COBROS SIN COMBOS")) {
			if (combo != null || !combo.isEmpty()) {
				msgReport = "No se esperaba un combo para la prueba " + tipoCobro + ", se continuara la automatización, pera revisar la información.";
				eventStatus = Reporter.MIC_WARNING;
				Reporter.reportEvent(eventStatus, msgReport);
			}
//			Evidence.save("Evidencia Inicial", this);
			Evidence.save("Evidencia Inicial");
			results = this.pageCombosCobros.getResultsSinCombo(servicio, combo);

			// verifica que no tenga combo
			String verificarCombo = results.get(0).get("Verificacion Combo");
			if (verificarCombo.contains("Empresa tiene combo")) {
				String comboNoDiligenciado = results.get(0).get("Combo encontrado no diligenciado");
				combo = comboNoDiligenciado;
				msgReport = "No se esperaba el combo '" + combo + "' para el tipo de prueba '" + tipoCobro
						+ "' para la empresa, se continuara la automatización, pera revisar la información desde cobro con combos.";
				eventStatus = Reporter.MIC_WARNING;
				Reporter.reportEvent(eventStatus, msgReport);
				results = this.pageCombosCobros.getResultsConCombo(combo, servicio);
			} else {
				msgReport = "La empresa no tiene combo como se esperaba.";
				eventStatus = Reporter.MIC_INFO;
				Reporter.reportEvent(eventStatus, msgReport);
			}
		}

		// Si el caso de prueba es con combos es porque hay un combo en el excel
		if (tipoCobro.equals("COBROS CON COMBOS")) {

			if (combo.isEmpty()) {
				msgReport = "Se esperaba un combo para la prueba " + tipoCobro
						+ ", pero no fue ingresado, revisar la información.";
				eventStatus = Reporter.MIC_FAIL;
				Reporter.reportEvent(eventStatus, msgReport);
				SettingsRun.exitTestIteration();
			}

			Evidence.save("Evidencia Inicial");
			results = this.pageCombosCobros.getResultsConCombo(combo, servicio);

			// verifica que tenga combo
			String verificarCombo = results.get(0).get("Verificacion Combo");
			if (verificarCombo.contains("Empresa no tiene combo")) {
				String empresaSinCombo = results.get(0).get("Empresa sin combo");
				combo = empresaSinCombo;
				msgReport = "No se esperaba el combo '" + combo
						+ "' para la empresa, se continuara la automatización, pera revisar la información desde cobro sin combos.";
				eventStatus = Reporter.MIC_WARNING;
				Reporter.reportEvent(eventStatus, msgReport);
				results = this.pageCombosCobros.getResultsSinCombo(servicio, combo);
			} else {
				msgReport = "La empresa tiene combo como se esperaba.";
				eventStatus = Reporter.MIC_INFO;
				Reporter.reportEvent(eventStatus, msgReport);

			}
		}

		// Si el campo en el excel se encuentra vacio, no se continuara la ejecuci�n
		if (tipoCobro.isEmpty()) {
			eventStatus = Reporter.MIC_FAIL;
			Reporter.reportEvent(eventStatus, "Se esparaba un tipo de cobro, pero el campo se encuentra vacio.");
			SettingsRun.exitTestIteration();
		}

		return results;
	}

	/**
	 * Retornara si el aumento por transacci�n es el correcto
	 * 
	 * @param informacionContarTx
	 * @throws Exception
	 */

	public void contadorFinal(int[] informacionContarTx, String verificarCombo) throws Exception {
		this.pageCombosCobros = new PageAdminCombosCobros(this);

		String tipoCobro = SettingsRun.getTestData().getParameter("Tipo Prueba Cobros");
		// Se da cambia el texto de servicio
		String servicioExcel = SettingsRun.getTestData().getParameter("Servicio");
		String servicio = PageServicios.getNbLinkServicio(servicioExcel);
		Evidence.save("Evidencia Final");
		if (tipoCobro.equals("COBROS CON COMBOS") && !verificarCombo.contains("Empresa no tiene combo")
				|| tipoCobro.equals("COBROS SIN COMBOS") || !verificarCombo.contains("Empresa no tiene combo")) {
			if (!servicio.equals("Pago de Servicios")) {

				this.irAOpcion(null, "Administración", "Empresa");

				this.pageEmpresaCobros.consultarEmpresa();
				this.pageEmpresaCobros.validarContadorPorTx();

				int[] validacion = this.pageEmpresaCobros.getContadorAct();
				for (int i = 0; i < validacion.length; i++) {
					DXCUtil.wait(2);
					if (validacion[i] != informacionContarTx[i]) {
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"El conteo final no son iguales: " + validacion[i] + " y " + informacionContarTx[i]);
						Evidence.save("Evidencia Los datos no coinciden");
						SettingsRun.exitTestIteration();
						break;
					}
				}
				Evidence.save("Evidencia Final");

				Reporter.reportEvent(Reporter.MIC_PASS, "El conteo de transacciones ha pasado exitosamente.");
			} else {
				Reporter.reportEvent(Reporter.MIC_PASS, "El conteo de transacciones no aplica para servicios.");

			}
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "El conteo de transacciones No se realiza para cobro sin combos.");
			SettingsRun.exitTestIteration();
		}
	}

}
