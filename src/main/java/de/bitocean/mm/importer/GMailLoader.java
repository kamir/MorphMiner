/*
 * Copyright 2014 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitocean.mm.importer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.security.auth.login.LoginException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class GMailLoader {

    static String zkHostString = null;
    static SolrServer solr = null;

    public static void init() throws Exception {
        // zkHostString = "training01.sjc.cloudera.com:2181,training03.sjc.cloudera.com:2181,training06.sjc.cloudera.com:2181/solr";
        zkHostString = "127.0.0.1:2181/solr";
        solr = new CloudSolrServer(zkHostString);
    }

    public static String[] importEmailsForLabels(String[] labels) throws LoginException, IOException, SolrServerException {

        Vector<String> list = new Vector<String>();

        String[] senderList = null;

        Properties props = System.getProperties();

        props.setProperty("mail.imaps.host", "imap.gmail.com");
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.port", "993");

        String account = javax.swing.JOptionPane.showInputDialog("Username:", "mirko.kaempf@cloudera.com");
        String pwd = javax.swing.JOptionPane.showInputDialog("Password:", "");

        for( String label : labels) {
        
            try {

                Session session = Session.getDefaultInstance(props, null);

                Store store = session.getStore("imaps");
                store.connect("imap.gmail.com", account, pwd);

                Folder[] f2 = store.getDefaultFolder().list();

                for (Folder fd : f2) {
                    System.out.println("Open Email-Account: (" + account + ") \n>> Open folder: " + fd.getName());
                }

                System.out.print(">>> import mails labeled with label=:" + label);

                Folder f = store.getFolder(label);
                f.open(Folder.READ_ONLY);
                Folder[] f3 = f.list();
                System.out.println("\n #of entries in this folder z=" + f3.length);

                int i = 0;
                for (Folder fd : f3) {
                    i++;
                    System.out.println("("+i+") > folder: " + fd );
                    importMailsFromFolder(fd, label);
                }
            } 
            catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return senderList;
    }

    private static void importMailsFromFolder(Folder f, String label) throws MessagingException, LoginException, IOException, SolrServerException {

        System.out.println("**** Import an email folder *** \n" + f.getFullName());

        f.open(Folder.READ_ONLY);

        Message[] m2 = f.getMessages();

        for (Message m : m2) {

            System.out.println("[[Mail Label:" + f.getFullName()
                    + "]]        \n# Subject: > " + m.getSubject());

            UUID uid = UUID.randomUUID();
            System.out.println("# UUID:    > " + uid.toString());

            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", uid.toString());
            document.addField("tag", m.getSubject());

            String cont = handleContent(document, m);
            //document.addField("contextdescription", cont );
            document.addField("answer", cont );
            
            document.addField("question", m.getSubject() );

            document.addField("type", "raw");
            document.addField("context", label);

            String author = "MAILIMPORT";
            Address[] from = m.getFrom();
            if (from != null) {
                author = "";
                for (Address a : from) {                    
                    author = author.concat(" " + a.toString());
                }
            }
            document.addField("author", author);

            Date d = m.getReceivedDate();
            
            DateFormat dfmt1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dfmt2 = new SimpleDateFormat("hh:mm:ss");
            document.addField("timeasked", dfmt1.format(d) + "T" + dfmt2.format(d) + "Z" );

            UpdateRequest add = new UpdateRequest();
            add.add(document);
            add.setParam("collection", "faq_collection1");
            add.process(solr);

        }

        UpdateRequest commit = new UpdateRequest();
        commit.setAction(UpdateRequest.ACTION.COMMIT, true, true);
        commit.setParam("collection", "faq_collection1");
        commit.process(solr);

    }

    private static String handleContent(SolrInputDocument document, Message m) throws IOException, MessagingException {
        String cont = "n.a.";
        try {
            
            Object content = m.getContent();
            if (content == null) {
                return cont;
            }

            if (content instanceof String) {
                String body = (String) content;
                cont = body;
            } else if (content instanceof Multipart) {

                MimeMultipart mmp = (MimeMultipart) content;
                for (int i = 0; i < mmp.getCount(); i++) {
                    MimeBodyPart mbp = (MimeBodyPart) mmp.getBodyPart(i);
                    String c = (String) mbp.getContent();
                    cont = cont + c + "\n\n\n" + "*** PART " + (i + 1) + " ***";
                }
            }
        } catch (Exception ex) {

        }

        return cont;

    }
}
