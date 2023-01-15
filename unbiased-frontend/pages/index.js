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
import {useTriggerEffect, useURLWithParams} from "../utils";
import CancelSearchLink from "../components/CancelSearchLink";
import ProfileCardTypeTab from "../components/ProfileCardTypeTab";
import Creator from "../components/Creator";
import Pagination from "../components/Pagination";
import baseURL from "./back";
import usePagination from "../pagination";
import {userMapper} from "../mappers"

const urlBase = new URL('users', baseURL)

export async function getServerSideProps(context) {
  // const users = await (await fetch(urlBase.href)).json()
  // return {
  //   props: {news,
  //     topCreators: users,
  //     creators: users},
  // }
  return {props: {}}
}

const parse = require('parse-link-header');


export default function Home(props) {
  const router = useRouter()
  const [newsEffectTrigger, newsTriggerEffect] = useTriggerEffect()
  const [useNews, setNews] = useState([])
  const [useUsers, setUsers] = useState([])
  const [topCreators, setTopCreators] = useState([])
  // const [pagination, setPagination] = useState(null)
  const [pagination, setPagination] = usePagination()
  const {I18n} = useAppContext()
  const maybeCurrent = parseInt(router.query.page || '1')
  const setParams = useURLWithParams()


  useEffect(() => {
    if (router.query.search && router.query.type === 'creator') {
      setParams(urlBase, ['type'])
      // alert(urlBase.href)
      fetch(urlBase.href).then(res=> {
        setPagination(res).then(() => {
          if (res.status !== 204)
            res.json().then(list => {setUsers(list.map(userMapper))
          })
        })
      })
      // const aux = props.creators
      // const first = aux[0]
      // aux[0] = aux[1]
      // aux[1] = first
      // setUsers(aux)
    } else {
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
    }
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    // const aux = props.topCreators
    // const first = aux[0]
    // aux[0] = aux[1]
    // aux[1] = first
    // setTopCreators(aux)
  }, [newsEffectTrigger])

  return (
    <>
    <Head>
      <title>unbiased - Home </title>
    </Head>
      {router.query.search ? <></> : <NewsCategoryTabs></NewsCategoryTabs>}
      <div className="d-flex flex-column flex-xl-row  flex-grow-1">
        <div className="w-100 w-xl-75 ">
          <TopNewTabs></TopNewTabs>
          {router.query.search ? <><CancelSearchLink text={I18n("search.filter", [router.query.search])}></CancelSearchLink> <ProfileCardTypeTab></ProfileCardTypeTab></> : <></>}
          <div className="container-fluid">
            <MainCardsContainer rows={2}>
              {/*{useNews.map( n => <Article triggerEffect={newsTriggerEffect} setNews={setNews} key={n.id} {...n}></Article>)}*/}
              { router.query.search && router.query.type === 'creator' ? useUsers.map(c => <Creator key={`creator${c.id}`} {...c}></Creator>) :
                  useNews.map(c => <Article triggerEffect={newsTriggerEffect} key={`article${c.id}`} {...c}></Article>)}
            </MainCardsContainer>
          </div>
        </div>
        <TopCreatorsPanel triggerEffect={newsTriggerEffect} creators = {topCreators.map(c => <TopCreator key={c.id} {...c}></TopCreator>)}></TopCreatorsPanel>
      </div>
      <Pagination {...pagination}></Pagination>
    </>
  );
}
