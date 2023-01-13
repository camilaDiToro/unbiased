import { useAppContext } from "../context";
import Link from "next/link";
import {useRouter} from "next/router";
import {useState} from "react";

export default function Pagination(props){
  const {I18n} = useAppContext()
  const router = useRouter()
  const [currentPage, setCurrentPage] = useState(props.currentPage)
  let pages = [props.currentPage-1, props.currentPage, props.currentPage+1]

  pages = pages.filter(p => p>=1 && p<=props.lastPage)

  const onClick = (e) => {
    setCurrentPage(e)
  }

  return(
    <>
    <nav className="d-flex justify-content-center align-items-center">
      <ul className="pagination" >
        <Link  onClick={() => onClick(1)} href={{
          pathname: router.pathname,
          query: {...router.query, page: 1}
        }}>
          <li className="page-item">
            <div className={`page-link ${currentPage === 1 ? 'font-weight-bold'  : ''}`} >
              {I18n("home.pagination.first")}
            </div>
          </li>
        </Link>

        {pages.map(c => <Link onClick={() => onClick(c)} key={c} href={{
          pathname: router.pathname,
          query: {...router.query, page: c}
          }}>
            <li className="page-item">
              <div className={`page-link ${currentPage === c ? 'font-weight-bold'  : ''}`} >
                {c}
              </div>
            </li>
          </Link>)
          }

        <Link onClick={() => onClick(props.lastPage)} href={{
          pathname: router.pathname,
          query: {...router.query, page: props.lastPage}
        }}>
          <li className="page-item">
            <div className={`page-link ${currentPage === props.lastPage ? 'font-weight-bold'  : ''}`} >
              {I18n("home.pagination.last")}
            </div>
          </li>
        </Link>
      </ul>
    </nav>
    </>
  )
}