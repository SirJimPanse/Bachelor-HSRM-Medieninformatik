using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Client
{
    public interface IMqObserver
    {
        void Update(string topicName, string itemId, long publisher, string mqEvent);
    }

    public interface IMqObservable
    {
        void NotifyObservers(string itemId, long publisher, string mqEvent);
        void AddObserver(IMqObserver observer);
        void RemoveObserver(IMqObserver observer);
        void SetTopicName(string name);
        void SetBrokerUrl(string url);
        void UpdateConnection();
        void ClearAllObservers();
    }
}