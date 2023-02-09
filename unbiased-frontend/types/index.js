import PropTypes from "prop-types";

const PositivityIndicator = {
    positivity: PropTypes.string.isRequired,
    upvoted: PropTypes.number.isRequired,
    interactions: PropTypes.number.isRequired,
    showNews: PropTypes.bool}

export const TopCreator = {
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

const Pagination =  {
    currentPage: PropTypes.number,
    lastPage: PropTypes.number
}

const Article = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
    body: PropTypes.string.isRequired,
    readTime: PropTypes.number.isRequired,
    saved: PropTypes.bool,
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
    stats: PropTypes.shape(PositivityIndicator).isRequired,
    triggerEffect: PropTypes.func.isRequired
}

const types = {
    Article,
    BackButton: {
        onClickHandler: PropTypes.func
    },
    Bookmark: {
        triggerEffect: PropTypes.func.isRequired,
        saved: PropTypes.bool,
        id: PropTypes.number.isRequired
    },
    CancelSearchLink: {
        text: PropTypes.string.isRequired
    },
    Creator: {
        ...TopCreator,
        isJournalist: PropTypes.bool.isRequired,
        hasPositivity: PropTypes.bool.isRequired,
        stats: PropTypes.shape(PositivityIndicator),
        admin: PropTypes.bool,
        toAdd: PropTypes.bool,
        triggerEffect: PropTypes.func
    },
    Comment: {
        id: PropTypes.number.isRequired,
        creator: PropTypes.shape(TopCreator),
        body: PropTypes.string,
        datetime: PropTypes.string.isRequired,
        upvotes: PropTypes.number,
        rating: PropTypes.number,
        deleted: PropTypes.bool,
        reported: PropTypes.bool,
        triggerEffect: PropTypes.func.isRequired
    },
    CommentButton: {
        id: PropTypes.number.isRequired,
    },
    CommentList: {
        comments: PropTypes.array.isRequired,
        pagination: PropTypes.shape(Pagination),
        triggerEffect: PropTypes.func.isRequired
    },
    EditProfileForm: {
        username: PropTypes.string,
        description: PropTypes.string,
        mailOptions: PropTypes.array,
        triggerEffect: PropTypes.func.isRequired
    },
    FollowButton: {
        userId: PropTypes.number.isRequired,
        following: PropTypes.bool.isRequired
    },
    FormattedDate : {
      datetime: PropTypes.string.isRequired,
      timeAgo: PropTypes.bool
    },
    Pagination,

    MainCardsContainer: {
        rows: PropTypes.number.isRequired
    },
    Modal: {
        id: PropTypes.string.isRequired,
        body: PropTypes.string,
        onClickHandler: PropTypes.func,
        onClickHandlerArray: PropTypes.array,
        title: PropTypes.string.isRequired,
        acceptText: PropTypes.string
    },
    ModalTrigger: {
        modalId: PropTypes.string.isRequired
    },
    NewsCategoryPills: {
        categories: PropTypes.array.isRequired
    },
    Navbar: {
        loggedUser: PropTypes.shape({...TopCreator, isAdmin: PropTypes.bool.isRequired}),
    },
    PositivityIndicator,
    ProfilePic: {
        tier: PropTypes.string
    },
    ProfileLink: {
        id: PropTypes.number.isRequired,
        bold: PropTypes.bool,
        shorten: PropTypes.bool,
        nameOrEmail: PropTypes.string.isRequired
    },

    ProgressBar,
    ReportedCard: {
      reportCount: PropTypes.number.isRequired,
      comment: PropTypes.bool,
      newsId: PropTypes.number,
      id: PropTypes.number.isRequired,
      title: PropTypes.string.isRequired,
      subtitle: PropTypes.string,
        datetime: PropTypes.string.isRequired,
        creator: PropTypes.shape(TopCreator),
        triggerEffect: PropTypes.func.isRequired
    },
    ReportReason: {
      user: PropTypes.string.isRequired,
      reason: PropTypes.string.isRequired,
      datetime: PropTypes.string.isRequired
    },
    Tabs: {
        items: PropTypes.arrayOf(PropTypes.shape({
            text: PropTypes.string.isRequired,
            params: PropTypes.objectOf(PropTypes.string)
        })),
        selected: PropTypes.string.isRequired,
        pill: PropTypes.bool,
        className: PropTypes.string
    },
    TopCreator,
    TopCreatorsPanel: {
        creators: PropTypes.array.isRequired
    },
    TopNewTabs: {
        className: PropTypes.string
    },
    ProfileTabs: {
        userId: PropTypes.number.isRequired
    },
    ReportedArticle: {
        title: PropTypes.string.isRequired,
        subtitle: PropTypes.string.isRequired,
        datetime: PropTypes.string.isRequired,
        reportCount: PropTypes.number.isRequired,
        creator: PropTypes.shape(TopCreator)
    },

    ReportFlag: {
        triggerEffect: PropTypes.func.isRequired,
        comment: PropTypes.bool,
        id: PropTypes.number.isRequired,
        reported: PropTypes.bool.isRequired
    },
    ReportForm: {
        triggerEffect: PropTypes.func.isRequired,
        comment: PropTypes.bool,
        id: PropTypes.number.isRequired,
        handlerArray: PropTypes.array.isRequired
    },
    Tooltip: {
        onClickHandler: PropTypes.func,
        className: PropTypes.string,
        position: PropTypes.string,
        text: PropTypes.string.isRequired
    },
    DeleteButton: {
        triggerEffect: PropTypes.func.isRequired,
        id: PropTypes.number.isRequired,
        comment: PropTypes.bool,
        creatorId: PropTypes.number,
        admin: PropTypes.bool
    },
    PinButton: {
        triggerEffect: PropTypes.func.isRequired,
        id: PropTypes.number.isRequired,
        pinned: PropTypes.bool.isRequired,
        creatorId: PropTypes.number.isRequired
    },
    UpvoteButtons: {
        rating: PropTypes.number,
        id: PropTypes.number.isRequired,
        triggerEffect: PropTypes.func.isRequired,
        comment: PropTypes.bool,
        upvotes: PropTypes.number.isRequired
    },
    UserPrivileges: {
        isJournalist: PropTypes.bool
    }


}

export default types;