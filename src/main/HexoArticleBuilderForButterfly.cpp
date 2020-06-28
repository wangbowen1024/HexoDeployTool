#include<iostream>
#include<fstream>
#include<string>
#include<ctime>
#include <vector>
#include<algorithm>
#include <string.h>
#include <stdlib.h>
#include <windows.h>
using namespace std;
vector<string> split(string str, string pattern)
{
	string::size_type pos;
	vector<string> result;

	str += pattern;//扩展字符串以方便操作
	int size = str.size();

	for (int i = 0; i < size; i++) {
		pos = str.find(pattern, i);
		if (pos < size) {
			std::string s = str.substr(i, pos - i);
			result.push_back(s);
			i = pos + pattern.size() - 1;
		}
	}
	return result;
}
string GbkToUtf8(const char *src_str)
{
	int len = MultiByteToWideChar(CP_ACP, 0, src_str, -1, NULL, 0);
	wchar_t* wstr = new wchar_t[len + 1];
	memset(wstr, 0, len + 1);
	MultiByteToWideChar(CP_ACP, 0, src_str, -1, wstr, len);
	len = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
	char* str = new char[len + 1];
	memset(str, 0, len + 1);
	WideCharToMultiByte(CP_UTF8, 0, wstr, -1, str, len, NULL, NULL);
	string strTemp = str;
	if (wstr) delete[] wstr;
	if (str) delete[] str;
	return strTemp;
}

int main() {
	// 基于当前系统的当前日期/时间
	time_t now = time(0);
	tm *ltm = localtime(&now);

	string name;
	string categories;
	string tags;
	cout << "-----Hexo基于Butterfly主题模板文章生成器-----" << endl;
	cout << "请输标题：" << endl << ">> ";
	cin >> name;
	cout << "请输分类（多级分类用空格隔开，如：'大数据 Hadoop'目前仅支持最多二级分类）：" << endl << ">> ";
	getchar();
	getline(cin, categories);
	cout << "请输入标签（多个标签用空格隔开）：" << endl << ">> ";
	getline(cin, tags);
	ofstream fout(name + ".md");
	if (fout) {
		vector<string> str_1;
		str_1 = split(categories, " ");
		vector<string> str_2;
		str_2 = split(tags, " ");
		fout << "---" << endl;
		fout << "title: " << GbkToUtf8(name.c_str()) << endl;
		char min[2];
		char sec[2];
		sprintf(min, "%02d", ltm->tm_min);
		sprintf(sec, "%02d", ltm->tm_sec);
		fout << "date: " << (1900 + ltm->tm_year) << "-" << (1 + ltm->tm_mon) << "-" << ltm->tm_mday << " " << ltm->tm_hour << ":" << min << ":" << sec << endl;
		fout << "updated: " << (1900 + ltm->tm_year) << "-" << (1 + ltm->tm_mon) << "-" << ltm->tm_mday << " " << ltm->tm_hour << ":" << min << ":" << sec << endl;
		fout << "categories: " << endl;
		if (str_1.size() == 1) {
			fout << "- " << GbkToUtf8(str_1[0].c_str()) << endl;
		}
		else {
			fout << "- [" << GbkToUtf8(str_1[0].c_str()) << ", " << GbkToUtf8(str_1[1].c_str()) << "]" << endl;
		}

		fout << "tags: " << endl;
		for (int i = 0; i < str_2.size(); i++) {
			fout << "- " << GbkToUtf8(str_2[i].c_str()) << endl;
		}
		fout << "cover: '/postImages/";
		if (str_1.size() == 1) {
			fout << GbkToUtf8(str_1[0].c_str());
		}
		else {
			fout << GbkToUtf8(str_1[1].c_str());
		}
		fout << "Cover.jpg'" << endl;

		fout << "comments: true" << endl;
		fout << "top: false" << endl;
		fout << "toc: true" << endl;
		fout << "toc_number: false" << endl;
		fout << "typora-root-url: ./" << endl;
		fout << "---" << endl;
		fout << "# " << GbkToUtf8(name.c_str()) << endl;
		fout.close(); // 执行完操作后关闭文件句柄

		system("cls");
		cout << "创建文章成功！>> " << name << ".md" << endl;
		cout << "==================================" << endl;
		cout << "---" << endl;
		cout << "title: " << name << endl;
		cout << "date: " << (1900 + ltm->tm_year) << "-" << (1 + ltm->tm_mon) << "-" << ltm->tm_mday << " " << ltm->tm_hour << ":" << min << ":" << sec << endl;
		cout << "updated: " << (1900 + ltm->tm_year) << "-" << (1 + ltm->tm_mon) << "-" << ltm->tm_mday << " " << ltm->tm_hour << ":" << min << ":" << sec << endl;
		cout << "categories: " << endl;
		if (str_1.size() == 1) {
			cout << "- " << str_1[0] << endl;
		}
		else {
			cout << "- [" << str_1[0] << ", " << str_1[1] << "]" << endl;
		}

		cout << "tags: " << endl;
		for (int i = 0; i < str_2.size(); i++) {
			cout << "- " << str_2[i] << endl;
		}
		cout << "cover: '/postImages/";
		if (str_1.size() == 1) {
			cout << str_1[0];
		}
		else {
			cout << str_1[1];
		}
		cout << "Cover.jpg'" << endl;

		cout << "comments: true" << endl;
		cout << "top: false" << endl;
		cout << "toc: true" << endl;
		cout << "toc_number: false" << endl;
		cout << "typora-root-url: ./" << endl;
		cout << "---" << endl;
		cout << "# " << name << endl;
		cout << "==================================" << endl;
		cout << "提示：" << endl;
		cout << "1.请在文章同目录下创建postImages文件夹用来放所有文章用到的图片" << endl;
		cout << "2.在文章中使用 ![avatar](/postImages/xxx.xxx)来引用图片" << endl;
		cout << "3.默认封面图片路径为/postImages/title + Cover.jpg";
		cout << "4.因为没有生成TOC目录，所以编辑时，建议打开编辑软件的侧边栏显示目录" << endl;
		cout << "参数介绍：" << endl;
		cout << "title:      文章标题" << endl;
		cout << "date:       文章创建日期" << endl;
		cout << "updated:    文章最后更新日期" << endl;
		cout << "categories: 文章分类" << endl;
		cout << "tags:       文章标签" << endl;
		cout << "cover:      文章封面图片" << endl;
		cout << "top:        文章是否置顶" << endl;
		cout << "toc:        是否在Hexo上为文章生成目录" << endl;
		cout << "toc_number: 是否给N级标题加上前缀数字（如果自己添加了，就选false）" << endl << endl << endl;
	}
	else {
		cout << "创建失败" << endl;
	}
	system("pause");
	return 0;
}
