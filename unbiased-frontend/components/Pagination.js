import { useAppContext } from "../context";
import Link from "next/link";

export default function Pagination(){
  const {I18n} = useAppContext()
  let pages = [1, 2, 3, 4]

  return(
    <>
    <nav class="d-flex justify-content-center align-items-center">
      <ul class="pagination" >

        <li class="page-item">
          <div class="page-link" >
            {I18n("home.pagination.first")}
          </div>
        </li>

        {pages.map(c => <Link key={c} href={{
          }}>
            <li class="page-item">
              <div class="page-link " >
                {c}
              </div>
            </li>
          </Link>)
          }

        <li class="page-item">
          <div class="page-link" >
          {I18n("home.pagination.last")}
          </div>
        </li>
      </ul>
    </nav>
    </>
  )
}