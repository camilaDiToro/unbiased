import Head from 'next/head'
import {useAppContext} from "../../context";
import {useRouter} from "next/router";
import CancelSearchLink from "../../components/CancelSearchLink";
import Creator from "../../components/Creator";
import Article from "../../components/Article";
import TopNewTabs from "../../components/TopNewTabs";
import PropTypes from "prop-types";
import TopCreatorsPanel from "../../components/TopCreatorsPanel";
import TopCreator from "../../components/TopCreator";
import MainCardsContainer from "../../components/MainCardsContainer";
import {news, users} from "../../hardcoded"
import {useEffect, useState} from "react";
import ProfileCardTypeTab from "../../components/ProfileCardTypeTab";


export async function getServerSideProps(context) {
    return {
        props: {news,
        topCreators: users,
        creators: users},
    }
}

export default function SearchPage(props) {
    const {I18n} = useAppContext()
    const router = useRouter()
    const ctx = useAppContext()
    const [useNews, setNews] = useState(props.news)
    const [useCreators, setCreators] = useState(props.creators)
    useEffect(() => {
        if (router.query.type === 'creator') {
            setCreators(props.creators)
        } else {
            setNews(props.news)
        }
    }, [router.query.type, router.query.order])

    const query = router.query.query

    const cards = router.query.type === 'creator' ? props.creators.map(c => <Creator key={`creator${c.id}`} {...c}></Creator>) :
        props.news.map(c => <Article key={`article${c.id}`} {...c}></Article>)

  return (
    <>
      <Head>
        <title>unbiased - Search</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
      <div className="d-flex flex-column flex-xl-row ">
          <div className="w-100 w-xl-75 ">
              <TopNewTabs className='my-4'></TopNewTabs>
              <CancelSearchLink text={ctx.I18n("search.filter", [query])}></CancelSearchLink>
              <ProfileCardTypeTab></ProfileCardTypeTab>
              <MainCardsContainer rows={query === 'creator' ? 3 : 2}>
                  {cards}
              </MainCardsContainer>
          </div>
          <TopCreatorsPanel creators={props.topCreators.map(c => <TopCreator key={c.id} {...c}></TopCreator>)}></TopCreatorsPanel>
      </div>
    </>
  )
}

SearchPage.propTypes = {
    news: PropTypes.arrayOf(PropTypes.instanceOf(Article)),
    creators: PropTypes.arrayOf(PropTypes.instanceOf(Creator)),
    topCreators: PropTypes.arrayOf(TopCreator)
}
