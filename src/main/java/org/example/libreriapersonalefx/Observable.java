package org.example.libreriapersonalefx;

public interface Observable {
        void addObserver(Observer o);
        void removeObserver(Observer o);
        void notifyObservers();

}
