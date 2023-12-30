package GUI.application.exceptionHandler;

import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateExceptionHandler implements ExceptionHandler {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(HibernateExceptionHandler.class);

	@Override
	public void handleException(Exception e) {
		logger.error("Hibernate error: ", e);

		JOptionPane.showMessageDialog(null,
				"A ocurrido un error en la base de datos. Por favor contactese con el administrador.",
				"Error en la base de datos",
				JOptionPane.ERROR_MESSAGE);
	}
}
