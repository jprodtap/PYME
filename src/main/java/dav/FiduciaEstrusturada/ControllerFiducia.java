package dav.FiduciaEstrusturada;

import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

public class ControllerFiducia {
	PageLoginPymes pageLogPymes = null;
	PageServiciosMiddle pageServiciosMiddle = null;
	PageFiduciaFront pageFiduciaFront = null;

	boolean servicioSeleccionado = false;
	int statusCase;
	public final static String INFORMACION_CONSULTA = "Seleccione la información a consultar:";

	public ControllerFiducia(PageLoginPymes pageParent) {
		pageLogPymes = pageParent;
	}
	
	/**
	 * Controla la información y reportes al finalizar la descarga del reporte
	 * @throws Exception
	 */
	public void consultasFiducia() throws Exception {
		
		this.pageFiduciaFront = new PageFiduciaFront(pageLogPymes);
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo Prueba");
		
		Reporter.write(">>> Prueba a realizar: " + tipoPrueba);
		
		//El metodo recibe el booleano para ser usado en los siguientes
		boolean msgError = this.pageFiduciaFront.fiduciaDocumentos();
		
		//Se inicializa principalmente como fallida, cambiara o se mantendra dependiendo la variable bool "msgError"
		int statusCase = Reporter.MIC_FAIL;
		
		//Almacena todos los mensajes 
		String msgReport[] = PageFiduciaFront.getMgsInformativo();

		if (msgError) {
			statusCase = Reporter.MIC_PASS;

			if (msgReport[0].contains("No se ha encontrado la opción") || msgReport[0].contains("Error data")
					|| msgReport[0].contains("No hay datos") || msgReport[0].contains("No se encontro información"))
				statusCase = Reporter.MIC_FAIL;

		} else {
			msgReport = PageFiduciaFront.getMgsInformativo();
		}
		
		//valida Y realiza el reporte de cada mensaje encontrado en msgReport
		for (int i = 0; i < msgReport.length; i++) {
			DXCUtil.wait(1);
			if (tipoPrueba.contains("Pendientes de descarga") && msgReport[i].contains("descargado"))
				statusCase = Reporter.MIC_PASS;
			else if (tipoPrueba.contains("Pendientes de descarga") && msgReport[i].contains("No hay datos"))
				statusCase = Reporter.MIC_WARNING;
			else
				Reporter.reportEvent(statusCase, msgReport[i]);
		}
	}

	/**
	 * Encargado de obtener el menu del caso de prueba
	 * @param tipoPrueba
	 * @return
	 */
	public static String getMenu(String tipoPrueba) {
		if (tipoPrueba.contains("-")) {
			tipoPrueba = tipoPrueba.split("-")[0].trim();
		}
		if (tipoPrueba.toUpperCase().contains("DETALLES")) {
			tipoPrueba = tipoPrueba.replace(" Detalles", "");
		}
		return tipoPrueba;
	}

	/**
	 * Encargado de obtener el submenu del caso de prueba
	 * @param tipoPrueba
	 * @return
	 */
	public static String getSubmenu(String tipoPrueba) {
		if (tipoPrueba.contains("-")) {
			String[] dividir = tipoPrueba.split("-");
			if (dividir[1].contains(">")) {
				String detalle = dividir[1].split(">")[0].trim();
				return detalle;
			}
			return dividir[1].trim();
		}

		if (tipoPrueba.contains("Detalles"))
			return tipoPrueba.replace(" Detalles", "").trim();

		return tipoPrueba;
	}

	/**
	 * Encargado de obtener el submenu2 del caso de prueba
	 * @param tipoPrueba
	 * @return
	 */
	public static String getSubmenu2(String tipoPrueba) {
		String[] dividirSubmenus = null;
		if (tipoPrueba.contains("-") && tipoPrueba.contains(">")) {
			String[] dividir = tipoPrueba.split("-");
			dividirSubmenus = dividir[1].split(">");
		}
		if (tipoPrueba.contains("-") && !tipoPrueba.contains(">")) {
			String[] dividir = tipoPrueba.split("-");
			return dividir[1].trim();
		}
		if (dividirSubmenus.length < 1)
			return "";

		return dividirSubmenus[1].trim();

	}

}
