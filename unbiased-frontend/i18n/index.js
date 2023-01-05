import { useRouter } from "next/router";

const strings = {
  "en-US": {
    "home.createArticle.button": "Crear",
    "views.register.title": "Register",
    "search.filter": (params) => `Cancel search filter: \"${params[0]}\"`,
    "search.notFound": (params) =>
      `We could not find any results that match \"${params[0]}\"`,
    "order.top": "Top",
    "order.new": "New",
    "home.topCreators": "Top Creators",
    "categories.notFound": (params) =>
      `We could not find any results for the category \"${params[0]}\"`,
    "categories.tourism": "Tourism",
    "categories.entertainment": "Entertainment",
    "categories.politics": "Politics",
    "categories.economics": "Economics",
    "categories.sports": "Sports",
    "categories.technology": "Technology",
    "categories.all": "All",
    "categories.forMe": "For me",
    "moderation.panel": "Moderation Panel",
    "moderation.reportedArticles": "Reported articles",
    "moderation.reportedComments": "Reported comments",
    "moderation.manage": "manage admins",
    "reportOrder.reportCountDesc": "Most Reported",
    "reportOrder.reportCountAsc": "Least Reported",
    "reportOrder.reportDateDesc": "Newest Reported",
    "reportOrder.reportDateAsc": "Oldest Reported",
  },
  "es-ES": {
    "home.createArticle.button": "Create cuio",
    "moderation.panel": "Panel de moderación",
    "moderation.reportedArticles": "Noticias reportadas",
    "moderation.reportedComments": "Comentarios reportados",
    "moderation.manage": "Gestionar admins",
    "reportOrder.reportCountDesc": "Más Reportados",
    "reportOrder.reportCountAsc": "Menos Reportados",
    "reportOrder.reportDateDesc": "Reportados más recientes",
    "reportOrder.reportDateAsc":"Reportados más antiguos",

  },
};

const I18n = (code, params) => {
  const { locale } = useRouter();
  const maybeFunc = strings[locale][code];
  if (typeof maybeFunc == "function") {
    return maybeFunc(params);
  }

  return maybeFunc;
};

export default I18n;
