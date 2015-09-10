#endif // IDLEDETECTOR_H
#include "idledetector.h"

/*#include 
#include 
#include 
#include */

IdleDetector::IdleDetector(int idleMaxSecs)
{
    m_idleMaxSecs = idleMaxSecs;
}

void IdleDetector::start() {
    timer = new QTimer();
    connect(timer, SIGNAL(timeout()), this, SLOT(timeout()));
    timer->start(1000);
}

void IdleDetector::stop() {
    timer->stop();
}

void IdleDetector::timeout() {
    bool _idleDetectionPossible;
    XScreenSaverInfo *_mit_info;

    int event_base, error_base;
    if(XScreenSaverQueryExtension(QX11Info::display(), &event_base, &error_base))
        _idleDetectionPossible = true;
    else
        _idleDetectionPossible = false;
    _mit_info = XScreenSaverAllocInfo();

    XScreenSaverQueryInfo(QX11Info::display(), QX11Info::appRootWindow(), _mit_info);

    long idlesecs = (_mit_info->idle/1000);

    if (idlesecs > m_idleMaxSecs) {
        timer->stop();
        idleTimeOut();
    }
}

