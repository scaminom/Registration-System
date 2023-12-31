package GUI.application.form.other;

import com.scrum.registrationsystem.service.Repository;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelManager {

	private Repository repository;

	public PanelManager(Repository repository) {
		this.repository = repository;
	}

	public void loadDataToFields(JTable table, Consumer<Object[]> consumer) {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(null, "Por favor, seleccione un registro de la tabla");
		} else {
			int columnCount = table.getColumnCount();
			Object[] rowData = new Object[columnCount];

			for (int i = 0; i < columnCount; i++) {
				rowData[i] = table.getValueAt(row, i);
			}

			consumer.accept(rowData);
		}
	}

	public void loadDataToTable(JTable table, BiConsumer<List<Object>, DefaultTableModel> consumer) {
		var tableModel = (DefaultTableModel) table.getModel();
		var entity = this.repository.findAll();
		tableModel.setRowCount(0);
		consumer.accept(entity, tableModel);
	}

	public void loadFormattedDataToTable(JTable table, BiConsumer<List<Object[]>, DefaultTableModel> consumer) {
		var tableModel = (DefaultTableModel) table.getModel();
		var objects = this.repository.findFormattedAll();
		tableModel.setRowCount(0);
		consumer.accept(objects, tableModel);
	}

	@SuppressWarnings("unchecked")
	public void insertData(Function<Void, Object> function) {

		var entity = function.apply(null);
		var entityCreated = this.repository.create(entity);

		if (entityCreated == true) {
			JOptionPane.showMessageDialog(null, "Se ha insertado correctamente los datos");
		} else {
			JOptionPane.showMessageDialog(null, "Hubo un error al insertar los datos");
		}
	}

	public void updateData(JTable table, Function<Object, Object> function) {

		var row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(null, "Por favor, seleccione un registro de la tabla");
		} else {
			var id = Long.valueOf(table.getValueAt(row, 0).toString());
			var foundEntity = this.repository.findById(id);
			var fieldsChanged = function.apply(foundEntity);
			var updateObject = this.repository.update(fieldsChanged);

			if (updateObject == true) {
				JOptionPane.showMessageDialog(null, "Se ha actualizado correctamente los datos");
			} else {
				JOptionPane.showMessageDialog(null, "Hubo un error al actualizar los datos");
			}
		}
	}

	public void deleteData(JTable table) {
		var row = table.getSelectedRow();
		var id = (Long) table.getValueAt(row, 0);
		var entityDeleted = this.repository.delete(id);
		if (entityDeleted == true) {
			JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente el registro");
		} else {
			JOptionPane.showMessageDialog(null, "Hubo un error al eliminar el registro");
		}
	}

}
