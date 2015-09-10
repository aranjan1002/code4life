#ifndef IDLEDETECTOR_H
#define IDLEDETECTOR_H

/*#include 
#include */

class IdleDetector : public QObject
{
   Q_OBJECT

public:
    IdleDetector(int idleMax);
    void start();
    void stop();

signals:
    void idleTimeOut();

private:
    int m_idleMaxSecs;
    QTimer* timer;

private slots:
    void timeout();

};

