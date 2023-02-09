import Head from "next/head";
import { useAppContext } from "../context";
import Article from "../components/Article";
import {useRouter} from "next/router";
import TopCreatorsPanel from "../components/TopCreatorsPanel";
import TopNewTabs from "../components/TopNewTabs";
import TopCreator from "../components/TopCreator";
import MainCardsContainer from "../components/MainCardsContainer";
import NewsCategoryTabs from "../components/NewsCategoryTabs";
import {useEffect, useState} from "react";
import {useTriggerEffect} from "../utils";
import CancelSearchLink from "../components/CancelSearchLink";
import ProfileCardTypeTab from "../components/ProfileCardTypeTab";
import Creator from "../components/Creator";
import Pagination from "../components/Pagination";
import usePagination from "../pagination";
import TimeSelector from "../components/TimeSelector";

export default function Home(props) {
  const router = useRouter()
  const [newsEffectTrigger, newsTriggerEffect] = useTriggerEffect()
  const [useNews, setNews] = useState([])
  const [useUsers, setUsers] = useState([])
  const [topCreators, setTopCreators] = useState([])
  const [pagination, setPagination] = usePagination()
  const {I18n,  api} = useAppContext()



  useEffect(() => {
    const params = {...router.query}
    delete params['type']
    if (router.query.search && router.query.type === 'creator') {

      api.getUsers(params).then(r => {
        const {success, data} = r
        if (success)
          setUsers(data)
      })

    } else {
        api.getArticles(params).then(r => {
          const {success, data, pagination} = r
          if (success) {
            setNews(data)
            setPagination(pagination)
          }

        })
      }
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    const params = {...router.query, topCreators: true}
    delete params['type']

      api.getUsers(params).then(res => {
        const {success, data} = res
        success && setTopCreators(data)
      })
  }, [newsEffectTrigger])

  return (
    <>
    <Head>
      <title>unbiased - Home</title>
    </Head>
      {router.query.search  ? <></> : <NewsCategoryTabs></NewsCategoryTabs>}
      <div className="d-flex flex-column flex-xl-row  flex-grow-1">
        <div className="w-100 w-xl-75 ">
          <TopNewTabs>
            {(router.query.search && router.query.type === 'creator' )|| (router.query.order === 'NEW')? <></> : <TimeSelector></TimeSelector>}
          </TopNewTabs>
          {router.query.search ? <><CancelSearchLink text={I18n("search.filter", [router.query.search])}></CancelSearchLink> <ProfileCardTypeTab></ProfileCardTypeTab></> : <></>}
          <div className="container-fluid">
            <MainCardsContainer rows={2}>
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
