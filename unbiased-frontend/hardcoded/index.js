const PropTypes = require("prop-types");
const news = [
  {
    title: "Title",
    subtitle: "Subtitle",
    body: "<h1>asjkbas jkas askj aksj asjk as</h1>",
    readTime: 3,
    saved: true,
    rating: 1,
    pinned: true,
    hasImage: false,
    datetime: "2022-10-10 14:48:00",
    reported: false,
    upvotes: 5,
    reportsCount: 3,
    categories: ["SHOW", "TOURISM"],
    creator: {
      nameOrEmail: "kevin",
      id: 5,
      hasImage: false,
      tier: "platinum",
    },
    id: 5,
    stats: {
      positivity: "positive",
      interactions: 20,
      upvoted: 0.8,
    },
    comments: [{id: 2,creator: {
        nameOrEmail: "kevin",
        id: 5,
        hasImage: false,
        tier: "platinum",
      }, body: 'HOlaaaaaaa', datetime: "2022-10-10 14:48:00", upvotes:3, rating: -1, reportsCount: 4}]
  },
  {
    title: "Title",
    subtitle: "Subtitle",
    body: "asjkbas jkas askj aksj asjk as",
    readTime: 3,
    saved: true,
    reported: false,
    pinned: false,
    reportsCount: 3,
    categories: ["SHOW", "TOURISM"],
    hasImage: false,
    datetime: "2011-10-10 14:48:00",
    rating: -1,
    upvotes: 5,
    creator: {
      nameOrEmail: "username",
      id: 4,
      hasImage: false,
      tier: "platinum",
    },
    id: 3,
    stats: {
      positivity: "positive",
      interactions: 20,
      upvoted: 0.8,
    },
  },
  {
    title: "Title",
    subtitle: "Subtitle",
    body: "asjkbas jkas askj aksj asjk as",
    readTime: 3,
    saved: true,
    reportsCount: 3,
    upvotes: 5,
    reported: true,
    pinned: false,
    rating: 1,
    hasImage: false,
    categories: ["SHOW", "TOURISM"],
    datetime: "2011-10-10 14:48:00",

    creator: {
      nameOrEmail: "username",
      id: 4,
      hasImage: false,
      tier: "platinum",
    },
    id: 6,
    stats: {
      positivity: "positive",
      interactions: 20,
      upvoted: 0.8,
    },
  },
];

const users = [
  {
    nameOrEmail: "Juan",
    hasImage: false,
    id: 2,
    tier: "platinum",
    isJournalist: true,
    stats: {
      positivity: "positive",
      interactions: 20,
      upvoted: 0.8,
    },
  },
  {
    nameOrEmail: "Juana",
    hasImage: false,
    id: 3,
    tier: "gold",
    isJournalist: false,
  },
];

const reportInfo = [
  {
    user: "Aleca",
    reason: "The content is not serious.",
    datetime: "2011-10-10 14:48:00",
    id: 1,
  },
  {
    user: "Kevin",
    reason: "The content is not serious.",
    datetime: "2011-10-10 14:48:00",
    id: 2,
  },
];

module.exports = { users, news, reportInfo };
