import PropTypes from "prop-types";

const PositivityIndicator = {
    positivity: PropTypes.string.isRequired,
    upvoted: PropTypes.number.isRequired,
    interactions: PropTypes.number.isRequired}

const TopCreator = {
    nameOrEmail: PropTypes.string.isRequired,
    hasImage: PropTypes.bool.isRequired,
    id: PropTypes.number.isRequired,
    tier: PropTypes.string.isRequired,
    image: PropTypes.string
}

const ProgressBar = {
    progress: PropTypes.number.isRequired,
    title: PropTypes.string.isRequired,
    i18n: PropTypes.bool
}

const Article = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
    body: PropTypes.string.isRequired,
    readTime: PropTypes.number.isRequired,
    saved: PropTypes.bool.isRequired,
    hasImage: PropTypes.bool.isRequired,
    image: PropTypes.string,
    creator: PropTypes.shape(TopCreator),
    id: PropTypes.number.isRequired,
    canDelete: PropTypes.bool,
    profileArticle: PropTypes.bool,
    pinned: PropTypes.bool,
    upvotes: PropTypes.number.isRequired,
    rating: PropTypes.number,
    datetime: PropTypes.string.isRequired,
    stats: PropTypes.shape(PositivityIndicator).isRequired
}

const types = {
    ShowNews: {...Article, categories: PropTypes.arrayOf(PropTypes.string), reported: PropTypes.bool},
    Article,
    BackButton: {
        onClickHandler: PropTypes.func
    },
    CancelSearchLink: {
        text: PropTypes.string.isRequired
    },
    Creator: {
        ...TopCreator,
        isJournalist: PropTypes.bool.isRequired,
        hasPositivity: PropTypes.bool.isRequired,
        stats: PropTypes.shape(PositivityIndicator)
    },
    FollowButton: {
        userId: PropTypes.number.isRequired
    },
    MainCardsContainer: {
        rows: PropTypes.number.isRequired
    },
    Modal: {
        body: PropTypes.string,
        onClickHandler: PropTypes.func,
        title: PropTypes.string.isRequired,
        acceptText: PropTypes.string
    },
    ModalTrigger: {
        modalId: PropTypes.string.isRequired
    },
    ModerationPanel: 'a',
    Navbar: {
        loggedUser: PropTypes.shape({...TopCreator, isAdmin: PropTypes.bool.isRequired}),
    },
    PanelCard: 'a',
    PositivityIndicator,
    ProfilePic: {
        tier: PropTypes.string.isRequired
    },
    ProgressBar,
    Tabs: {
        items: PropTypes.arrayOf(PropTypes.shape({
            text: PropTypes.string.isRequired,
            params: PropTypes.objectOf(PropTypes.string)
        })),
        selected: PropTypes.string.isRequired,
        pill: PropTypes.bool
    },
    TopCreator,
    TopCreatorsPanel: 'a',
    TopNewTabs: {
        className: PropTypes.string
    },
    ProfileTabs: {
        userId: PropTypes.number.isRequired
    },
    Profile: {
        isJournalist: PropTypes.bool.isRequired,
        news: PropTypes.arrayOf(PropTypes.shape(Article)),
        email: PropTypes.string.isRequired,
        username: PropTypes.string,
        tier: PropTypes.string.isRequired,
        followers: PropTypes.number.isRequired,
        following: PropTypes.number.isRequired,
        description: PropTypes.string,
        isLoggedUserFollowing: PropTypes.bool,
        stats: PropTypes.shape(PositivityIndicator),
        mailOptions: PropTypes.arrayOf(PropTypes.string),
        newsStatistics: PropTypes.arrayOf(PropTypes.shape(ProgressBar))
    },
    ReportedArticle: {
        title: PropTypes.string.isRequired,
        subtitle: PropTypes.string.isRequired,
        datetime: PropTypes.string.isRequired,
        reportsCount: PropTypes.number.isRequired,
        creator: PropTypes.shape(TopCreator)
    },
    Bookmark: {
        triggerEffect: PropTypes.func.isRequired,
        saved: PropTypes.bool,
        id: PropTypes.number.isRequired
    },
    ReportFlag: {
        triggerEffect: PropTypes.func.isRequired,
        comment: PropTypes.bool,
        id: PropTypes.number.isRequired,
    },
    DeleteButton: {
        triggerEffect: PropTypes.func.isRequired,
        id: PropTypes.number.isRequired,
        comment: PropTypes.bool,
        creatorId: PropTypes.number
    },
    PinButton: {
        triggerEffect: PropTypes.func.isRequired,
        id: PropTypes.number.isRequired,
        pinned: PropTypes.bool.isRequired,
        creatorId: PropTypes.number.isRequired
    },
    Comment: {
        id: PropTypes.number.isRequired,
        creator: TopCreator,
        body: PropTypes.string.isRequired,
        datetime: PropTypes.string.isRequired,
        upvotes: PropTypes.number.isRequired,
        rating: PropTypes.number,
    },
    CommentButton: {

    },
}

export default types;