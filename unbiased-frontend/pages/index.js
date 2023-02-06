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
import {useLoggedParamsFiller, useTriggerEffect, useURLWithParams} from "../utils";
import CancelSearchLink from "../components/CancelSearchLink";
import ProfileCardTypeTab from "../components/ProfileCardTypeTab";
import Creator from "../components/Creator";
import Pagination from "../components/Pagination";
import baseURL from "./back";
import usePagination from "../pagination";
import {newsMapper, userMapper} from "../mappers"
import TimeSelector from "../components/TimeSelector";


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
  const setParams = useURLWithParams()

  const {I18n, axios, loggedUser, jwtState} = useAppContext()
  const [jwt, setJwt] = jwtState
  const {fillNewsLoggedParams} = useLoggedParamsFiller()

  const getUsersData = async res => {
    const data = res.data || []

    const finalData = data.map(d => {
      const aux = d
      delete aux['newsStats']
      return aux
    })

    setUsers(finalData ? finalData.map(userMapper) : [])
  }



  useEffect(() => {
    const params = {...router.query}
    delete params['type']
    if (router.query.search && router.query.type === 'creator') {

      axios.get('users', {params}).then(getUsersData)

    } else {
        axios.get('news', {params}).then(res => {
          setPagination(res)
        const mappedNews = (res.data || []).map(newsMapper)
          fillNewsLoggedParams(mappedNews).then(n => setNews(n))
      })
      }
      // setNews(res.data ? res.data.map(newsMapper) : [])
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    const params = {...router.query, topCreators: true}
    delete params['type']

      axios.get('users', {params}).then(res => {
        const topCreators = res.data.map(u => {
          delete u['newsStats']
          return userMapper(u)
        })
        setTopCreators(topCreators)
      })
  }, [newsEffectTrigger])

  return (
    <>
    <Head>
      <title>unbiased - Home </title>
    </Head>
      {router.query.search  ? <></> : <NewsCategoryTabs></NewsCategoryTabs>}
      <div className="d-flex flex-column flex-xl-row  flex-grow-1">
        <div className="w-100 w-xl-75 ">
          <TopNewTabs>
            {router.query.search && router.query.type === 'creator' || (!router.query.order  || router.query.order === 'NEW')? <></> : <TimeSelector></TimeSelector>}
          </TopNewTabs>
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
