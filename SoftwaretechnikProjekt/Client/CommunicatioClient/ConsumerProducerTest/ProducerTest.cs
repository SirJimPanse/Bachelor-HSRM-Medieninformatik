using ObjectTest;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using Apache.NMS;

namespace ConsumerProducerTest
{
    
    
    /// <summary>
    ///Dies ist eine Testklasse für "ProducerTest" und soll
    ///alle ProducerTest Komponententests enthalten.
    ///</summary>
    [TestClass()]
    public class ProducerTest
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
        ///Ein Test für "Producer-Konstruktor"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void ProducerConstructorTest()
        {
            string topicName = string.Empty; // TODO: Passenden Wert initialisieren
            string brokerURL = string.Empty; // TODO: Passenden Wert initialisieren
            Producer_Accessor target = new Producer_Accessor(topicName, brokerURL);
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
            Producer_Accessor target = new Producer_Accessor(param0); // TODO: Passenden Wert initialisieren
            object sender = null; // TODO: Passenden Wert initialisieren
            EventArgs e = null; // TODO: Passenden Wert initialisieren
            target.CleanupResources(sender, e);
            Assert.Inconclusive("Eine Methode, die keinen Wert zurückgibt, kann nicht überprüft werden.");
        }

        /// <summary>
        ///Ein Test für "Send"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void SendTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Producer_Accessor target = new Producer_Accessor(param0); // TODO: Passenden Wert initialisieren
            string s = string.Empty; // TODO: Passenden Wert initialisieren
            target.Send(s);
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
            Producer_Accessor target = new Producer_Accessor(param0); // TODO: Passenden Wert initialisieren
            target.UpdateConnection();
            Assert.Inconclusive("Eine Methode, die keinen Wert zurückgibt, kann nicht überprüft werden.");
        }

        /// <summary>
        ///Ein Test für "MessageProducer"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void MessageProducerTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Producer_Accessor target = new Producer_Accessor(param0); // TODO: Passenden Wert initialisieren
            IMessageProducer actual;
            actual = target.MessageProducer;
            Assert.Inconclusive("Überprüfen Sie die Richtigkeit dieser Testmethode.");
        }

        /// <summary>
        ///Ein Test für "Session"
        ///</summary>
        [TestMethod()]
        [DeploymentItem("ObjectTest.exe")]
        public void SessionTest()
        {
            PrivateObject param0 = null; // TODO: Passenden Wert initialisieren
            Producer_Accessor target = new Producer_Accessor(param0); // TODO: Passenden Wert initialisieren
            ISession actual;
            actual = target.Session;
            Assert.Inconclusive("Überprüfen Sie die Richtigkeit dieser Testmethode.");
        }
    }
}
