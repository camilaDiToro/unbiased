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

  const {I18n, axios, setErrorDetails, jwtState} = useAppContext()
  const [jwt, setJwt] = jwtState
  const maybeCurrent = parseInt(router.query.page || '1')


  useEffect(() => {
    const params = {...router.query}
    delete params['type']
    if (router.query.search && router.query.type === 'creator') {

      // axios.get('users', {params}).then(res => {
      //   setUsers(res.data ? res.data.map(userMapper) : [])
      // })
      axios.put(`users/2/pingNews/4`, {}).catch(error => {
        if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.log(error.response.data);
          console.log(error.response.status);
          console.log(error.response.headers);
          setErrorDetails(error.response.data)
        } else if (error.request) {
          // The request was made but no response was received
          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
          // http.ClientRequest in node.js
          console.log(error.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log('Error', error.message);
        }
      })

    } else {
      setNews(n => {
        axios.get('news', {params}).then(res => {
          setNews(res.data ? res.data.map(newsMapper) : [])
        })
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
          <TopNewTabs>
            <TimeSelector></TimeSelector>
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
