import Head from "next/head";
import Tabs from "../components/Tabs";
import { useAppContext } from "../context";
import Article from "../components/Article";
import {useRouter} from "next/router";
import TopCreatorsPanel from "../components/TopCreatorsPanel";

export default function Home() {
  const router = useRouter()



  const ctx = useAppContext();


  const I18n = ctx.I18n
  const categories = [
    { text: I18n("categories.all"), params: {cat: 'ALL'} },
    { text: I18n("categories.tourism"), params: {cat: 'TOURISM'} },
    { text: I18n("categories.entertainment"), params: {cat: 'SHOW'} },
    { text: I18n("categories.politics"), params: {cat: 'POLITICS'} },
    { text: I18n("categories.economics"), params: {cat: 'ECONOMICS'} },
    { text: I18n("categories.sports"), params: {cat: 'SPORTS'} },
    { text: I18n("categories.technology"), params: {cat: 'TECHNOLOGY'} }
  ];

  const categoryMap = categories.reduce((a,v) => ({...a, [v.params.cat]: v.text}), {})

  const selectedCategory = categoryMap[router.query.cat] || I18n("categories.all")

  const orders = [
    { text: I18n("order.new"), params: {order: 'NEW'} },
    { text: I18n("order.top"), params: {order: 'TOP'} }
  ];

  const orderMap = orders.reduce((a,v) => ({...a, [v.params.order]: v.text}), {})


  const selectedOrder = orderMap[router.query.order] || I18n("order.top");


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
  const topCreators = [{ nameOrEmail: "Juan" , id: 5, hasImage: false}, { nameOrEmail: "Juana" , id: 6, hasImage: false}];

  return (
    <>
    <Head>
      <title>unbiased - Home</title>
    </Head>
      <Tabs items={categories} selected={selectedCategory}></Tabs>
      <div className="d-flex flex-column flex-xl-row ">
        <div className="w-100 w-xl-75 ">
          <Tabs items={orders} pill selected={selectedOrder}></Tabs>
          <div className="container-fluid">
            <div className="row row-cols-1 row-cols-md-2">
              {news.map((n) => (
                <Article {...n} key={n.id}></Article>

              ))}
            </div>
          </div>
        </div>
        <TopCreatorsPanel creators = {topCreators}></TopCreatorsPanel>
      </div>
    </>
  );
}
