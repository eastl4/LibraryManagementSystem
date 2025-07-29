package org.library.Observer;

import javafx.collections.ObservableList;
import org.library.entity.Book;

public interface BookInventoryObserver extends Observer<ObservableList<Book>> {
}
