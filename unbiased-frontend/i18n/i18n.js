import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import errors from "../errors";

const es = {
    //NavBar
    "navbar.search": "Buscar",
    "navbar.logout": "Cerrar Sesión",
    "navbar.myProfile": "Mi Perfil",
    "navbar.logIn": "Iniciar Sesión",
    "navbar.register": "Registrarme",
    "navbar.adminPanel": "Panel de Administración",

    //Home
    "home.createArticle.button": "Crear artículo",
    "home.emptyCreators": "No ha habido actividad aún.",
    "home.topCreators": "Creadores populares",
    "home.type.article":"Noticias",
    "home.type.creator":"Creadores",
    "home.upvotes": `{{count}}% gustado`,
    "home.read":  `{{count}} min para leer`,
    "home.interactions":  `{{count}} interacción`,
    "home.interactions_plural":  `{{count}} interacciones`,
    "search.filter":  `Cancelar filtro de búsqueda: \"{{count}}\"`,
    "search.notFound": `No se encontraron resultados para la búsqueda \"{{count}}\"`,
    "home.pagination.first": "Inicio",
    "home.pagination.last": "Fin",

    //Categories
    "order.top": "Más popular",
    "order.new": "Nuevo",
    "categories.notFound":  `No se encontraron resultados para la categoría \"{{count}}\"`,
    "categories.tourism": "Turismo",
    "categories.entertainment": "Espectáculo",
    "categories.politics": "Política",
    "categories.economics": "Economía",
    "categories.sports": "Deportes",
    "categories.technology": "Tecnología",
    "categories.all": "Todo",
    "categories.forMe": "Para mi",

    //Profile
    "profile.edit": "Editar",
    "profile.modal.question": "¿Estás seguro de que quieres eliminar esta noticia?",
    "profile.modal.msg": "Al eliminar de esta noticia no podra volver a recuperarla. ¿EstÃás seguro de que deseas continuar?",
    "profile.modal.accept": "Confirmar",
    "profile.user.settings": "Ajustes de usuario",
    "profile.modal.save": "Guardar",
    "profile.modal.changeUsername": "Nombre de usuario",
    "profile.modal.changeProfilePicture": "Imágen de perfil",
    "profile.modal.changeDescription": "Descripción",
    "profile.modal.changeMailOptions": "Notificarme por correo cuando los siguientes eventos ocurren:",
    "profile.follow": "Seguir",
    "profile.following": "Siguiendo",
    "profile.modal.infoTitle": "Información de la cuenta",
    "profile.modal.infoAllowedMsg": "Tenes permitido:",
    "profile.modal.infoChangeUsername": "Cambiar tu nombre de usuario",
    "profile.modal.infoChangeProfileimg": "Cambiar tu imagen de perfil",
    "profile.modal.infoChangeAddDescription": "Añadir una descripción",
    "profile.modal.enabled": "Habilitado",
    "profile.modal.disabled": "Deshabilitado",
    "profile.pin.question": "¿Estás seguro de que quieres fijar esta noticia?",
    "profile.pin.body": "Al fijar esta noticia podrás visualizarla sobre las demas noticias. ¿Estás seguro de que deseas continuar?",
    "profile.unpin.question": "¿EstÃ¡s seguro de que quieres desfijar esta noticia?",
    "profile.unpin.body": "Al desfijar esta noticia no podrás visualizarla sobre las demas noticias. ¿Estás seguro de que deseas continuar?",
    "profile.followers": "Seguidores",
    "profileCategories.myPosts": "Noticias",
    "profileCategories.saved": "Guardados",
    "profileCategories.upvoted": "Me gusta",
    "profileCategories.downvoted": "No me gusta",
    "profile.notFound": `No se encontraron resultados para el filtro \"{{count}}\"`,

    //Error
    "error.articleNotFound": "El artículo buscado no existe.",
    "error.commentNotFound": "El comentario buscado no existe.",
    "error.userNotFound": "El usuario buscado no existe.",
    "error.invalidCategory": "Categoría inválida.",
    "error.userNotAuthorized": "Usuario no autorizado.",
    "error.invalidOrder": "Parámetro de orden inválido.",
    "error.invalidFilter": "Filtro inválido.",
    "genericError.ops": "Upss!",
    "genericError.message.400": "Solicitud incorrecta.",
    "genericError.message.401": "Sesión no autorizada.",
    "genericError.message.403": "No tienes permisos para acceder a esta URL.",
    "genericError.message.404": "Página no encontrada.",
    "genericError.message.405": "Método no permitido.",
    "genericError.message.500": "Hubo un problema de nuestro lado.",
    "genericError.button.goHome": "Volver al inicio",

    //Create article
    "createArticle.title": "Título",
    "createArticle.title.placeholder": "Ejemplo de título",
    "createArticle.description": "Descripción",
    "createArticle.description.placeholder": "Ejemplo de descrpición",
    "createArticle.body": "Cuerpo de la noticia",
    "createArticle.category.choose": "Elegir categoría",
    "createArticle.save": "Guardar",
    "createArticle.label": "Navegar",
    "createArticle.modal.question": "¿Estás seguro de que quieres descartar esta noticia?",
    "createArticle.modal.msg": "Al salir de esta página, se perderá todo el progreso y los cambios no serán guardados. ¿Estás seguro de que deseas continuar?",
    "createArticle.modal.accept": "Confirmar",
    "createArticle.modal.cancel": "Cancelar",
    "createArticle.selectFile": "Elija el archivo",
    "createArticle.imageMsg": "Imágen de la noticia",

    //Register
    "register.mail.address": "Dirección de mail",
    "register.password": "Contraseña",
    "register.signUp": "Registrarse",
    "register.alreadyMemberQuestion": "¿Ya eres miembro?",

    //Verify email
    "verifyEmail.goHome": "Ir a la página principal",
    "verifyEmail.login": "Ir a iniciar sesión",
    "verificationToken.succesfullyVerified": "Email verificado exitosamente.",
    "verificationToken.succesfullyResended": "Verificación de email reenviada.",
    "verificationToken.expired": "El token de verificación expiró.",
    "verificationToken.notExists": "El token de verificación no existe.",
    "verificationToken.alreadyVerified": "Esta cuenta ya fue verificada.",

    //Login
    "login.error": "Hubo un error con los datos ingresados.",
    "login.mail.address": "Dirección de mail",
    "login.password": "Contraseña",
    "login.rememberMe": "Recordarme",
    "login.signIn": "Iniciar Sesión",
    "login.emailNotVerified": "El email no se encuentra verificado.",
    "login.emailResended": "El link de confirmación fue reenviado.",
    "login.resendVerificationEmail.button": "Reenviar verificación de email",

    //Moderation panel
    "moderation.panel": "Panel de moderación",
    "moderation.emptyArticles": "No hay noticias reportadas todavía.",
    "moderation.reportedComments": "Comentarios reportados",
    "moderation.emptyComments": "No hay comentarios reportados todavía.",
    "moderation.emptyAdmins": "No hay administradores todavía.",
    "moderation.emptyAdminsSearch": `Ningún administrador coincide con \"{{count}}\"`,
    "moderation.details": "Ver detalles",
    "moderation.reportedArticles": "Noticias reportadas",
    "moderation.delete": "Borrar noticia",
    "moderation.deleteComment": "Borrar comentario",
    "moderation.user": "Usuario",
    "moderation.reason": "Razón",
    "moderation.date": "Fecha",
    "moderation.reportsNum": "Cantidad de reportes",
    "moderation.makeUserAdmin": "Hacer administrador a un usuario",
    "moderation.admin.succesfull": "Admininstrador agregado exitosamente!",
    "moderation.add": "Agregar administrador",
    "moderation.manage": "Manejar administradoress",
    "moderation.searchAdmin": "Buscar en panel de administradores",
    "reportOrder.reportCountDesc": "Más Reportados",
    "reportOrder.reportCountAsc": "Menos Reportados",
    "reportOrder.reportDateDesc": "Reportados más recientes",
    "reportOrder.reportDateAsc": "Reportados más antiguos",
    "report.inappropiate": "El contenido es inapropiado.",
    "report.notSerious": "El contenido no es serio.",
    "report.violent": "El contenido es violento.",
    "report.lie": "Hay contenido que no es verdad en la noticia.",

    //Show news
    "showNews.reportNewsQuestion": "¿Estás seguro de que quieres reportar esta noticia?",
    "showNews.comment": "Comentario",
    "showNews.comments": "Comentarios",
    "showNews.comment.submit": "Comentar",
    "showNews.categories": "Categorías:",
    "showNews.emptyCommentsLogged": "No hay comentarios.",
    "showNews.deleteComment": "Borrar comentario",
    "showNews.deleteCommentQuestion": "¿Estás seguro de que quieres eliminar este comentario?",
    "showNews.deleteCommentBody": "Al eliminar este comentario, no podrás volver a recuperarlo. ¿Estás seguro de que deseas continuar?",
    "showNews.deletedComment": "El comentario ha sido eliminado.",
    "showNews.reportCommentQuestion": "¿Estás seguro de que quieres reportar este comentario?",
    "showNews.commentLogged": " para comentar!",

    //Tooltips
    "tooltip.clickToGoBack": "Click para volver hacia atras",
    "tooltip.clickToGoHome": "Click para ir a la página principal",

    "tooltip.deleteNews": "Borrar noticia",
    "tooltip.articleReported": "Noticia reportada",
    "tooltip.commentReported": "Comentario reportado",
    "tooltip.reportComment": "Reportar comentario",
    "tooltip.reportArticle": "Reportar noticia",
    "tooltip.articleSave": "Guardar noticia",
    "tooltip.articleUnsave": "Deshacer guardado de noticia",
    "tooltip.commentArticle": "Comentar la noticia",
    "tooltip.pin": "Fijar noticia",
    "tooltip.unpin": "Desfijar noticia",
    "tooltip.info": "Información",
    "tooltip.infoDisabled": "Para habilitar esta caracteristica debes crear tu primer artículo",
    "tooltip.addAdmin": "Añadir administradores",
    "tooltip.removeAdmin": "Borrar administradores",

    // Mail options
    "mailOption.follow":"Nuevos seguidores",
    "mailOption.comment":"Nuevos comentarios",
    "mailOption.folowingPublished": "Un periodista que sigo publica una noticia",
    "mailOption.positivityChanged": "El valor de positividad de mi noticia cambia",

    //Owner
    "owner.removeAdminTitle": "¿Está seguro de que quiere quitar los privilegios de administrador?",
    "owner.removeAdminMsg": "Si acepta, el usuario no podrá ingresar más al panel de administradores.",
    "owner.addAdminTitle": "¿Está seguro de que quiere otorgar privilegios de administrador?",
    "owner.addAdminMsg": "Si acepta, el usuario podrá ingresar al panel de administradores.",

    // Time Constraint
    "timeConstraint.hour":"Última hora",
    "timeConstraint.day":"Hoy",
    "timeConstraint.week":"Última semana",
    "timeConstraint.alltime":"Todo el tiempo"

}

const en = {

        //NavBar
        "navbar.search": "Search",
        "navbar.logout": "Log out",
        "navbar.myProfile": "My profile",
        "navbar.logIn": "Log in",
        "navbar.register": "Sign up",
        "navbar.adminPanel": "Administration panel",

        //Home
        "home.createArticle.button": "Create",
        "home.emptyCreators": "There hasn't been any activity yet.",
        "home.topCreators": "Top Creators",
        "home.type.article":"Articles",
        "home.type.creator":"Creators",
        "home.upvotes": `{{count}}% upvoted`,
        "home.read": `{{count}} min read`,
        "home.interactions":  `{{count}} interaction`,
        "home.interactions_plural":  `{{count}} interactions`,

        // "home.interactions_plural":  `{{count}} interactions`,
        "search.filter":  `Cancel search filter: \"{{count}}\"`,
        "search.notFound":
            `We could not find any results that match \"{{count}}\"`,
        "home.pagination.first": "First",
        "home.pagination.last": "Last",

        //Categories
        "order.top": "Top",
        "order.new": "New",
        "categories.notFound": `We could not find any results for the category \"{{count}}\"`,
        "categories.tourism": "Tourism",
        "categories.entertainment": "Entertainment",
        "categories.politics": "Politics",
        "categories.economics": "Economics",
        "categories.sports": "Sports",
        "categories.technology": "Technology",
        "categories.all": "All",
        "categories.forMe": "For me",

        //Profile
        "profile.edit": "Edit",
        "profile.modal.question": "Are you sure you want to delete the article?",
        "profile.modal.msg": "Accepting this you will lose the article and you will not be able to recover it again. Are you sure you would like to continue?",
        "profile.modal.accept": "Confirm",
        "profile.user.settings": "User settings",
        "profile.modal.save": "Save",
        "profile.modal.changeUsername": "Username",
        "profile.modal.changeProfilePicture": "Profile picture",
        "profile.modal.changeDescription": "Description",
        "profile.modal.changeMailOptions": "Notify me by email when the following events occur:",
        "profile.follow": "Follow",
        "profile.following": "Following",
        "profile.modal.infoTitle": "Account information",
        "profile.modal.infoAllowedMsg": "You are allowed to:",
        "profile.modal.infoChangeUsername": "Change your username",
        "profile.modal.infoChangeProfileimg": "Change your profile image",
        "profile.modal.infoChangeAddDescription": "Add a description",
        "profile.modal.enabled": "Enabled",
        "profile.modal.disabled": "Disabled",
        "profile.pin.question": "Are you sure you want to pin the article?",
        "profile.pin.body": "Pinning this article it will appear on top of the rest of the news. Are you sure you would like to continue?",
        "profile.unpin.question": "Are you sure you want to unpin the article?",
        "profile.unpin.body": "Unpinning this article it will stop appearinf on top of the rest of the news. Are you sure you would like to continue?",
        "profile.followers": "Followers",
        "profileCategories.saved": "Saved",
        "profileCategories.upvoted": "Upvoted",
        "profileCategories.downvoted": "Downvoted",
        "profileCategories.myPosts": "Posts",
        "profile.notFound": `No results found for filter \"{{count}}\"`,

        //Error
        "error.articleNotFound": "The news article you're looking for doesn't exist.",
        "error.commentNotFound": "The comment you're looking for doesn't exist.",
        "error.userNotFound": "The user you're looking for doesn't exist.",
        "error.invalidCategory": "Invalid category.",
        "error.userNotAuthorized": "User not authorized.",
        "error.invalidOrder": "Invalid order path.",
        "error.invalidFilter": "Invalid filter.",
        "genericError.ops": "Oops!",
        "genericError.message.400": "Bad request.",
        "genericError.message.401": "Unauthorized session.",
        "genericError.message.403": "You don't have authorization to enter the specified URL.",
        "genericError.message.404": "Page not found.",
        "genericError.message.405": "Method not allowed.",
        "genericError.message.500": "There was a problem on our side.",
        "genericError.button.goHome": "Go Home",

        //Create article
        "createArticle.title": "Title",
        "createArticle.title.placeholder": "This is a title example",
        "createArticle.description": "Description",
        "createArticle.description.placeholder": "This is a description example",
        "createArticle.body": "Body",
        "createArticle.category.choose": "Choose a category",
        "createArticle.save": "Save",
        "createArticle.label": "Browse",
        "createArticle.modal.question": "Discard the changes?",
        "createArticle.modal.msg": "Navigating away from this page will lose all the progress and your changes will not be saved. Are you sure you would like to continue?",
        "createArticle.modal.accept": "Confirm",
        "createArticle.modal.cancel": "Cancel",
        "createArticle.selectFile": "Choose file",
        "createArticle.imageMsg": "Image of the notice",

        //Register
        "register.mail.address": "Email address",
        "register.password": "Password",
        "register.signUp": "Sign Up",
        "register.alreadyMemberQuestion": "Already a member?",

        //Verify email
        "verifyEmail.goHome": "Go home",
        "verifyEmail.login": "Go to login",
        "verificationToken.succesfullyVerified": "Email verification successful.",
        "verificationToken.succesfullyResended": "Verification email resended.",
        "verificationToken.expired": "The verification token has expired.",
        "verificationToken.notExists": "The verification token does not exist.",
        "verificationToken.alreadyVerified": "This account has already been verified.",

        //Login
        "login.error": "There was an error with the inputted values",
        "login.mail.address": "Email address",
        "login.password": "Password",
        "login.rememberMe": "Remember me",
        "login.signIn": "Sign in",
        "login.emailNotVerified": "The email is not verified.",
        "login.emailResended": "The confirmation link has been resended.",
        "login.resendVerificationEmail.button": "Resend verification email",

        //Moderation panel
        "moderation.panel": "Moderation Panel",
        "moderation.emptyArticles": "There are no reported articles yet.",
        "moderation.reportedComments": "Reported comments",
        "moderation.emptyComments": "There are no reported comments yet.",
        "moderation.emptyAdmins": "There are no admins yet.",
        "moderation.emptyAdminsSearch":   `No admins match \"{{count}}\"`,
        "moderation.details": "View details",
        "moderation.reportedArticles": "Reported articles",
        "moderation.delete": "Delete article",
        "moderation.deleteComment": "Delete comment",
        "moderation.user": "User",
        "moderation.reason": "Reason",
        "moderation.date": "Date",
        "moderation.reportsNum": "Number of reports",
        "moderation.makeUserAdmin": "Make user admin",
        "moderation.admin.succesfull": "Admin added successfully!",
        "moderation.add": "Add admins",
        "moderation.manage": "Manage admins",
        "moderation.searchAdmin": "Search in admin panel",
        "reportOrder.reportCountDesc": "Most Reported",
        "reportOrder.reportCountAsc": "Least Reported",
        "reportOrder.reportDateDesc": "Newest Reported",
        "reportOrder.reportDateAsc": "Oldest Reported",
        "report.inappropiate": "The content is inappropriate.",
        "report.notSerious": "The content is not serious.",
        "report.violent": "The content is violent.",
        "report.lie": "The content is not true.",

        //Show news
        "showNews.reportNewsQuestion": "Are you sure you want to report this article?",
        "showNews.comments": "Comments",
        "showNews.comment.submit": "Comment",
        "showNews.comment": "Comment",
        "showNews.categories": "Categories:",
        "showNews.emptyCommentsLogged": "Nobody has commened yet.",
        "showNews.deleteComment": "Delete comment",
        "showNews.deleteCommentQuestion": "Are you sure you want to delete the comment?",
        "showNews.deleteCommentBody": "Accepting this you will lose the comment, so you will not be able to recover it again. Are you sure you would like to continue?",
        "showNews.deletedComment": "The comment has been deleted.",
        "showNews.reportCommentQuestion": "Are you sure you want to report the comment?",
        "showNews.commentLogged": " to comment!",

        //Tooltips
        "tooltip.clickToGoBack": "Click to go back",
    "tooltip.clickToGoHome": "Click to go to the home page",
    "tooltip.deleteNews": "Delete news",
        "tooltip.articleReported": "Article reported",
        "tooltip.commentReported": "Comment reported",
        "tooltip.reportComment": "Report comment",
        "tooltip.reportArticle": "Report article",
        "tooltip.articleSave": "Save article",
        "tooltip.articleUnsave": "Unsave article",
        "tooltip.commentArticle": "Comment article",
        "tooltip.info": "Information",
        "tooltip.addAdmin": "Add admins",
        "tooltip.removeAdmin": "Remove admins",
        "tooltip.pin": "Pin article",
        "tooltip.unpin": "Unpin article",
        "tooltip.infoDisabled": "To enable this option you need to create your first article",

        // Mail options
        "mailOption.follow":"New followers",
        "mailOption.comment":"New comments",
        "mailOption.folowingPublished": "A journalist I follow published an article",
        "mailOption.positivityChanged": "The positivity value of my news changes",

        //Owner
        "owner.removeAdminTitle": "Are you sure you want to revoke admin privileges?",
        "owner.removeAdminMsg": "By accepting the user will no longer be able to access the administration panel",
        "owner.addAdminTitle": "Are you sure you want to grant admin privileges?",
        "owner.addAdminMsg": "By accepting the user will be able to access the administration panel",

        // Time Constraint
        "timeConstraint.hour":"Last hour",
        "timeConstraint.day":"Today",
        "timeConstraint.week":"Last week",
        "timeConstraint.alltime":"All time"

    }

    for (const [key, value] of Object.entries(errors)) {
        en[`${key}`] = value.en
        es[`${key}`] = value.es
    }


i18n
    .use(initReactI18next) // passes customI18n down to react-i18next
    .init({
        compatibilityJSON: 'v3',
        resources: {
            es : {translation: es},
            en: {translation: en},
            },
        lng: "en",
        fallbackLng: "en",

        interpolation: {
            escapeValue: false,
        }
    });

export default i18n;