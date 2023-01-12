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
import {useTriggerEffect} from "../utils";

export async function getServerSideProps(context) {
  return {
    props: {news,
      topCreators: users },
  }
}

export default function Home(props) {
  const router = useRouter()
  const [newsEffectTrigger, newsTriggerEffect] = useTriggerEffect()
  const [useNews, setNews] = useState(props.news)
  const [topCreators, setTopCreators] = useState(props.topCreators)
  const [useA, setA] = useState(0)

  useEffect(() => {
    setNews(n => {
      for (const news of n) {
        switch(news.rating) {
          case 1: news.rating = 0;
            break;
          case 0: news.rating = -1;
            break;
          case -1:news.rating =  1;
        }
      }
      return n
    })
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    const aux = props.topCreators
    const first = aux[0]
    aux[0] = aux[1]
    aux[1] = first
    setTopCreators(aux)
  }, [newsEffectTrigger])

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
              {useNews.map( n => <Article triggerEffect={newsTriggerEffect} setNews={setNews} key={n.id} {...n}></Article>)}
            </MainCardsContainer>
          </div>
        </div>
        <TopCreatorsPanel triggerEffect={newsTriggerEffect} creators = {topCreators.map(c => <TopCreator key={c.id} {...c}></TopCreator>)}></TopCreatorsPanel>
      </div>
    </>
  );
}
