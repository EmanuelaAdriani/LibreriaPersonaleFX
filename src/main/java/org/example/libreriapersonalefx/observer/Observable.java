package org.example.libreriapersonalefx.observer;

public interface Observable {
        void addObserver(Observer o);
        void notifyObservers();

}
