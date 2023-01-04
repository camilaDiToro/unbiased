import Head from "next/head";
import Tabs from "../../../components/Tabs";
import { useAppContext } from "../../../context";
import ProfileArticle from "../../../components/ProfileArticle";

export default function Profile(props) {
  const items = [
    { text: "Hola", route: "/" },
    { text: "Como", route: "/" },
    { text: "Va", route: "/" },
  ];
  const ctx = useAppContext();
  const news = [{ id: 1 }, { id: 2 }];
  const selected = "Como";
  const topCreators = [{ name: "Juan" }, { name: "Lucio" }];
  const pinnedNews = undefined;

  return (
    <>
      <Head>
        <title>unbiased - Profile</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
      <Tabs items={items} selected={selected}></Tabs>
      <div className="d-flex flex-column h-100">
        <div className="flex-grow-1 d-flex flex-row">
          <div className="d-flex flex-column w-70 align-items-start">
            <div className="tab">
              <Tabs items={items} pill selected={selected}></Tabs>
            </div>
            <div className="tab">
              <div className="container-fluid">
                <div className="row row-cols-1">
                  {news.map((n) => (
                    <ProfileArticle key={n.id} id={n.id}></ProfileArticle>
                  ))}
                </div>
              </div>
            </div>
          </div>

          <div
            className="card container w-30 p-4 h-auto m-2 h-fit align-self-xl-start"
            id="none_shadow"
          ></div>
        </div>
      </div>
    </>
  );
}
