import Head from 'next/head'
import Tabs from "components/Tabs";
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


export async function getServerSideProps(context) {
    return {
        props: {news:  [
                {
                    title: 'Title',
                    subtitle: "Subtitle",
                    body: "asjkbas jkas askj aksj asjk as",
                    readTime: 3,
                    saved: true,
                    hasImage: false,
                    creator: {
                        nameOrEmail: "username",
                        id: 4,
                        hasImage: false
                    },
                    id: 4,
                },
                {
                    title: "Title",
                    subtitle: "Subtitle",
                    body: "asjkbas jkas askj aksj asjk as",
                    readTime: 3,
                    saved: true,
                    hasImage: false,
                    creator: {
                        nameOrEmail: "username",
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
                        nameOrEmail: "username",
                        id: 4,
                        hasImage: false
                    },
                    id: 6,
                },
            ],
        topCreators: [{
            nameOrEmail: 'Juan',
            hasImage: false,
            id: 2,
        }, {
            nameOrEmail: 'Juana',
            hasImage: false,
            id: 3,
        }],
        creators: [{
            nameOrEmail: 'Kevin',
            hasImage: false,
            id: 1,
            isJournalist: true,
            stats: {positivity: 'negative',
                upvoted: 0.3,
                interactions: 20}
        }, {
            nameOrEmail: 'Ivan',
            hasImage: false,
            id: 2,
            isJournalist: false
        }]},
    }
}

export default function SearchPage(props) {
    const {I18n} = useAppContext()
    const router = useRouter()
    const ctx = useAppContext()
    const types = [{text: I18n("home.type.article"), params: {type: 'article'}}, {text: I18n("home.type.creator"), params: {type: 'creator'}}]
    const selectedType = (types.find(t => t.params.type === router.query.type) || types[0]).text
    const query = router.query.query

    const cards = router.query.type === 'creator' ? props.creators.map(c => <Creator key={`creator${c.id}`} {...c}></Creator>) :
        props.news.map(c => <Article key={`article${c.id}`} {...c}></Article>)
    // const cards = props.creators.map(c => <Creator key={c.id} {...c}></Creator>)

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
              <Tabs items={types} selected={selectedType}></Tabs>
              <MainCardsContainer rows={query === 'creator' ? 3 : 2} cards={cards}></MainCardsContainer>
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
