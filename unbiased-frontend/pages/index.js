import Head from 'next/head'
import Tabs from "../components/Tabs";
import Article from "../components/Article";

function SearchPage() {
    return <></>
}

function NormalPage() {
    return
}

export default function Home() {
    const items = [{text: "Hola", route: "/"}, {text: "Como", route: "/"}, {text: "Va", route: "/"}]
    const news = [{id: 1}, {id: 2}]
    const selected = "Como"
    const query = undefined
    const type = 'creator'

  return (
    <>
      <Head>
        <title>unbiased - Home</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
        <Tabs items={items} selected={selected}></Tabs>
      <div className="d-flex flex-column flex-xl-row ">
          <div className="w-100 w-xl-75 ">

              <Tabs items={items} pill selected={selected}></Tabs>
              <div className="container-fluid">
                  <div className="row row-cols-1 row-cols-md-2">
                      {news.map(n => <Article key={n.id}></Article>)}
                  </div>

              </div>
          </div>
          <div className="card container w-100 w-xl-25 p-4 h-auto m-2 h-fit align-self-xl-start" id="none_shadow">
          </div>
      </div>
    </>
  )
}
