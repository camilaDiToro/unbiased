const PropTypes = require("prop-types");
const news = [
  {
    title: "Title",
    subtitle: "Subtitle",
    body: "asjkbas jkas askj aksj asjk as",
    readTime: 3,
    saved: true,
    rating: 1,
    hasImage: false,
    datetime: "2022-10-10 14:48:00",

    upvotes: 5,
    reportsCount: 3,
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
  },
  {
    title: "Title",
    subtitle: "Subtitle",
    body: "asjkbas jkas askj aksj asjk as",
    readTime: 3,
    saved: true,
    reportsCount: 3,
    categories: [],
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
    rating: 1,
    hasImage: false,
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

module.exports = { users, news };
