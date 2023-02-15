import { useAppContext } from "../../context";
import Link from "next/link";

export default function GenericError(props){
  const {I18n}= useAppContext();
  const typeErrors= [
    { text: "error.articleNotFound", value: I18n("error.articleNotFound") },
    { text: "error.commentNotFound",value: I18n("error.commentNotFound" ) },
    { text: "error.userNotFound", value: I18n("error.userNotFound") },
    { text: "error.invalidCategory", value: I18n("error.invalidCategory") },
    { text: "error.userNotAuthorized", value: I18n("error.userNotAuthorized") },
    { text: "error.invalidOrder", value: I18n("error.invalidOrder") },
    { text: "error.invalidFilter", value: I18n("error.invalidFilter") }
  ]

  return(
  <>
    { typeErrors.map(c => <Link key={c} href={{
      pathname: "/${c}",
      query: { cat: c },
    }}>
            <h1 className="display-1 fw-bold">404</h1>
            <p className="fs-1">
              <span className="text-info font-weight-bold">{I18n("genericError.ops")}</span>
                {I18n("genericError.message.404")}
            </p>
            <p className="lead">
              {c.value}
            </p>
            <button className="btn btn-info" type="submit">{I18n("genericError.button.goHome")}</button>
    </Link>)}

  </>
  )
}