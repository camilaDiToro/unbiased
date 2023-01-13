import Head from "next/head";
import { useAppContext } from "../context";
import Article from "../components/Article";
import {useRouter} from "next/router";
import TopCreatorsPanel from "../components/TopCreatorsPanel";
import TopNewTabs from "../components/TopNewTabs";
import TopCreator from "../components/TopCreator";
import MainCardsContainer from "../components/MainCardsContainer";
import NewsCategoryTabs from "../components/NewsCategoryTabs";
import {news, users} from "../hardcoded"
import {useEffect, useState} from "react";
import Pagination from "../components/Pagination";

export async function getServerSideProps(context) {
  return {
    props: {news,
      topCreators: users },
  }
}

export default function Home(props) {
  const router = useRouter()

  const [useNews, setNews] = useState(props.news)
  const [useA, setA] = useState(0)

  useEffect(() => {
    setNews(props.news)
  }, [router.query.order, router.query.cat])



  return (
    <>
    {/*<Head>*/}
    {/*  <title>unbiased - Home </title>*/}
    {/*</Head>*/}
      <NewsCategoryTabs></NewsCategoryTabs>
      <div className="d-flex flex-column flex-xl-row ">
        <div className="w-100 w-xl-75 ">
          <TopNewTabs></TopNewTabs>
          <div className="container-fluid">
            <MainCardsContainer rows={2}>
              {useNews.map( n => <Article setNews={setNews} key={n.id} {...n}></Article>)}
            </MainCardsContainer>
          </div>
        </div>
        <TopCreatorsPanel creators = {props.topCreators.map(c => <TopCreator key={c.id} {...c}></TopCreator>)}></TopCreatorsPanel>
      </div>
      <Pagination></Pagination>
    </>
  );
}
