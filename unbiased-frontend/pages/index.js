import Head from "next/head";
import Tabs from "../components/Tabs";
import { useAppContext } from "../context";
import Link from "next/link";
import Article from "../components/Article";

export default function Home() {
  const items = [
    { text: "Hola", params: {param1: 'hola'} },
    { text: "Como", params: {param1: 'hosla'}},
    { text: "Va", params: {param1: 'hoasla'} },
  ];
  const ctx = useAppContext();
  const news = [
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
        hasImage: false
      },
      id: 5,
    },
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
        hasImage: false
      },
      id: 5,
    },
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
        hasImage: false
      },
      id: 5,
    },
  ];
  const selected = "Hola";
  const topCreators = [{ name: "Juan" }, { name: "Lucio" }];

  return (
    <>
    <Head>
      <title>unbiased - Home</title>
    </Head>
      <Tabs items={items} selected={selected}></Tabs>
      <div className="d-flex flex-column flex-xl-row ">
        <div className="w-100 w-xl-75 ">
          <Tabs items={items} pill selected={selected}></Tabs>
          <div className="container-fluid">
            <div className="row row-cols-1 row-cols-md-2">
              {news.map((n) => (
                <Article {...n} key={n.id}></Article>
              ))}
            </div>
          </div>
        </div>
        <div
          className="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start"
          id="none_shadow"
        >
          <h5
            style={{ backgroundImage: "url('/img/crown-svgrepo-com.svg')" }}
            className="card-title top-creators"
          >
            {ctx.I18n("home.topCreators")}
          </h5>

          {topCreators.length === 0 ? (
            <h6 className="text-info m-1">{ctx.I18n("home.emptyCreators")}</h6>
          ) : (
            <></>
          )}
          {topCreators.map((c) => (
            <Link key={c.name} className="m-1 link" href="">
              <div
                className="card text-white d-flex flex-row p-2 creator-card align-items-center"
                id="none_shadow_creator"
              >
                <div className="img-container">
                  <img
                    className="rounded-circle object-fit-cover mr-1"
                    src="/img/profile-image.png"
                    alt=""
                  />
                </div>
                <div className="mx-2 text-ellipsis-1">{c.name}</div>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </>
  );
}
