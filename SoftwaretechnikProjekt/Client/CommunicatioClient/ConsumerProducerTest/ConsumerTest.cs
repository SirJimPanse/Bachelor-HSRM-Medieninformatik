using ObjectTest;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using Apache.NMS;

namespace ConsumerProducerTest
{
    
    
    /// <summary>
    ///Dies ist eine Testklasse für "ConsumerTest" und soll
    ///alle ConsumerTest Komponententests enthalten.
    ///</summary>
    [TestClass()]
    public class ConsumerTest
    {


        private TestContext testContextInstance;

        /// <summary>
        ///Ruft den Testkontext auf, der Informationen
        ///über und Funktionalität für den aktuellen Testlauf bietet, oder legt diesen fest.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }

        #region Zusätzliche Testattribute
        // 
        //Sie können beim Verfassen Ihrer Tests die folgenden zusätzlichen Attribute verwenden:
        //
        //Mit ClassInitialize führen Sie Code aus, bevor Sie den ersten Test in der Klasse ausführen.
        //[ClassInitialize()]
        //public static void MyClassInitialize(TestContext testContext)
        //{
        //}
        //
        //Mit ClassCleanup führen Sie Code aus, nachdem alle Tests in einer Klasse ausgeführt wurden.
        //[ClassCleanup()]
        //public static void MyClassCleanup()
        //{
        //}
        //
        //Mit TestInitialize können Sie vor jedem einzelnen Test Code ausführen.
        //[TestInitialize()]
        //public void MyTestInitialize()
        //{
        //}
        //
        //Mit TestCleanup können Sie nach jedem einzelnen Test Code ausführen.
        //[TestCleanup()]
        //public void MyTestCleanup()
        //{
        //}
        //
        #endregion


        /// <summary>
        ///Ein Test für "Consumer-Konstruktor"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void ConsumerConstructorTest()
        {
            string topicName = string.Empty; // TODO: Passenden Wert initialisieren
            string brokerURL = string.Empty; // TODO: Passenden Wert initialisieren
            Consumer_Accessor target = new Consumer_Accessor(topicName, brokerURL);
            Assert.Inconclusive("TODO: Code zum Überprüfen des Ziels implementieren");
        }

        /// <summary>
        ///Ein Test für "CleanupResources"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void CleanupResourcesTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Consumer_Accessor target = new Consumer_Accessor(param0); // TODO: Passenden Wert initialisieren
            object sender = null; // TODO: Passenden Wert initialisieren
            EventArgs e = null; // TODO: Passenden Wert initialisieren
            target.CleanupResources(sender, e);
            Assert.Inconclusive("Eine Methode, die keinen Wert zurückgibt, kann nicht überprüft werden.");
        }

        /// <summary>
        ///Ein Test für "OnMessageReceived"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void OnMessageReceivedTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Consumer_Accessor target = new Consumer_Accessor(param0); // TODO: Passenden Wert initialisieren
            IMessage msg = null; // TODO: Passenden Wert initialisieren
            target.OnMessageReceived(msg);
            Assert.Inconclusive("Eine Methode, die keinen Wert zurückgibt, kann nicht überprüft werden.");
        }

        /// <summary>
        ///Ein Test für "UpdateConnection"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void UpdateConnectionTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Consumer_Accessor target = new Consumer_Accessor(param0); // TODO: Passenden Wert initialisieren
            target.UpdateConnection();
            Assert.Inconclusive("Eine Methode, die keinen Wert zurückgibt, kann nicht überprüft werden.");
        }
    }
}
